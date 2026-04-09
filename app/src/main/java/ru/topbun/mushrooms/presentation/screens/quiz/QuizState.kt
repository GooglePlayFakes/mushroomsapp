package ru.topbun.mushrooms.presentation.screens.quiz

import ru.topbun.mushrooms.domain.entity.MushroomProfileEntity
import ru.topbun.mushrooms.domain.entity.QuizQuestionEntity

data class QuizState(
    val profile: MushroomProfileEntity = MushroomProfileEntity(),
    val questions: List<QuizQuestionEntity> = emptyList(),
    val currentIndex: Int = 0,
    val selectedAnswer: String? = null,
    val isAnswerLocked: Boolean = false,
    val correctAnswers: Int = 0,
    val currentStreak: Int = 0,
    val bestStreak: Int = 0,
    val sporesEarned: Int = 0,
    val masteredMushroomIds: Set<Int> = emptySet(),
    val errorText: String = "",
    val screenState: QuizScreenState = QuizScreenState.Loading,
) {

    val currentQuestion get() = questions.getOrNull(currentIndex)
    val totalQuestions get() = questions.size

    sealed interface QuizScreenState {
        data object Loading : QuizScreenState
        data object Playing : QuizScreenState
        data object Finished : QuizScreenState
        data object Error : QuizScreenState
    }
}
