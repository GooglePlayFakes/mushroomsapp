package ru.topbun.mushrooms.data.repository

import android.app.Application
import android.content.Context
import androidx.core.content.edit
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.topbun.mushrooms.domain.entity.MushroomProfileEntity

class MushroomProfileRepository(application: Application) {

    private val preferences = application.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    init {
        synchronized(lock) {
            if (!initialized) {
                state.value = readProfile()
                initialized = true
            }
        }
    }

    fun observeProfile() = state.asStateFlow()

    suspend fun saveQuizResult(
        score: Int,
        totalQuestions: Int,
        earnedSpores: Int,
        bestStreak: Int,
        masteredMushroomIds: Set<Int>,
    ): MushroomProfileEntity = synchronized(lock) {
        val current = state.value
        val updated = current.copy(
            spores = current.spores + earnedSpores,
            quizzesCompleted = current.quizzesCompleted + 1,
            correctAnswers = current.correctAnswers + score,
            answeredQuestions = current.answeredQuestions + totalQuestions,
            bestScore = maxOf(current.bestScore, score),
            bestStreak = maxOf(current.bestStreak, bestStreak),
            perfectRounds = current.perfectRounds + if (score == totalQuestions) 1 else 0,
            masteredMushroomIds = current.masteredMushroomIds + masteredMushroomIds
        )
        persist(updated)
        state.value = updated
        updated
    }

    private fun persist(profile: MushroomProfileEntity) {
        preferences.edit {
            putInt(KEY_SPORES, profile.spores)
            putInt(KEY_QUIZZES_COMPLETED, profile.quizzesCompleted)
            putInt(KEY_CORRECT_ANSWERS, profile.correctAnswers)
            putInt(KEY_ANSWERED_QUESTIONS, profile.answeredQuestions)
            putInt(KEY_BEST_SCORE, profile.bestScore)
            putInt(KEY_BEST_STREAK, profile.bestStreak)
            putInt(KEY_PERFECT_ROUNDS, profile.perfectRounds)
            putStringSet(
                KEY_MASTERED_MUSHROOM_IDS,
                profile.masteredMushroomIds.map { it.toString() }.toSet()
            )
        }
    }

    private fun readProfile() = MushroomProfileEntity(
        spores = preferences.getInt(KEY_SPORES, 0),
        quizzesCompleted = preferences.getInt(KEY_QUIZZES_COMPLETED, 0),
        correctAnswers = preferences.getInt(KEY_CORRECT_ANSWERS, 0),
        answeredQuestions = preferences.getInt(KEY_ANSWERED_QUESTIONS, 0),
        bestScore = preferences.getInt(KEY_BEST_SCORE, 0),
        bestStreak = preferences.getInt(KEY_BEST_STREAK, 0),
        perfectRounds = preferences.getInt(KEY_PERFECT_ROUNDS, 0),
        masteredMushroomIds = preferences
            .getStringSet(KEY_MASTERED_MUSHROOM_IDS, emptySet())
            .orEmpty()
            .mapNotNull { it.toIntOrNull() }
            .toSet()
    )

    companion object {
        private const val PREFS_NAME = "mushroom_profile"
        private const val KEY_SPORES = "spores"
        private const val KEY_QUIZZES_COMPLETED = "quizzes_completed"
        private const val KEY_CORRECT_ANSWERS = "correct_answers"
        private const val KEY_ANSWERED_QUESTIONS = "answered_questions"
        private const val KEY_BEST_SCORE = "best_score"
        private const val KEY_BEST_STREAK = "best_streak"
        private const val KEY_PERFECT_ROUNDS = "perfect_rounds"
        private const val KEY_MASTERED_MUSHROOM_IDS = "mastered_mushroom_ids"

        private val lock = Any()
        private val state = MutableStateFlow(MushroomProfileEntity())
        private var initialized = false
    }
}
