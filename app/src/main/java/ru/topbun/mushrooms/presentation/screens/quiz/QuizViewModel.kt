package ru.topbun.mushrooms.presentation.screens.quiz

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.topbun.mushrooms.data.repository.MushroomProfileRepository
import ru.topbun.mushrooms.data.repository.QuizRepository
import ru.topbun.mushrooms.domain.useCases.CreateQuizRoundUseCase
import ru.topbun.mushrooms.domain.useCases.ObserveMushroomProfileUseCase
import ru.topbun.mushrooms.domain.useCases.SaveQuizResultUseCase
import ru.topbun.mushrooms.presentation.screens.quiz.QuizState.QuizScreenState

class QuizViewModel(application: Application) : AndroidViewModel(application) {

    private val quizRepository = QuizRepository(application)
    private val profileRepository = MushroomProfileRepository(application)

    private val createQuizRoundUseCase = CreateQuizRoundUseCase(quizRepository)
    private val observeMushroomProfileUseCase = ObserveMushroomProfileUseCase(profileRepository)
    private val saveQuizResultUseCase = SaveQuizResultUseCase(profileRepository)

    private val _state = MutableStateFlow(QuizState())
    val state = _state.asStateFlow()

    init {
        observeProfile()
        startNewRound()
    }

    fun startNewRound() = viewModelScope.launch {
        _state.update {
            it.copy(
                questions = emptyList(),
                currentIndex = 0,
                selectedAnswer = null,
                isAnswerLocked = false,
                correctAnswers = 0,
                currentStreak = 0,
                bestStreak = 0,
                sporesEarned = 0,
                masteredMushroomIds = emptySet(),
                errorText = "",
                screenState = QuizScreenState.Loading
            )
        }
        val questions = createQuizRoundUseCase()
        _state.update {
            if (questions.isEmpty()) {
                it.copy(
                    errorText = "Не удалось собрать маршрут квиза. Попробуй ещё раз.",
                    screenState = QuizScreenState.Error
                )
            } else {
                it.copy(
                    questions = questions,
                    screenState = QuizScreenState.Playing
                )
            }
        }
    }

    fun chooseAnswer(answer: String) {
        val stateValue = state.value
        val question = stateValue.currentQuestion ?: return
        if (stateValue.isAnswerLocked) return

        if (answer == question.correctAnswer) {
            val streak = stateValue.currentStreak + 1
            _state.update {
                it.copy(
                    selectedAnswer = answer,
                    isAnswerLocked = true,
                    correctAnswers = it.correctAnswers + 1,
                    currentStreak = streak,
                    bestStreak = maxOf(it.bestStreak, streak),
                    sporesEarned = it.sporesEarned + rewardFor(streak),
                    masteredMushroomIds = it.masteredMushroomIds + question.mushroomId
                )
            }
        } else {
            _state.update {
                it.copy(
                    selectedAnswer = answer,
                    isAnswerLocked = true,
                    currentStreak = 0
                )
            }
        }
    }

    fun moveNext() {
        val stateValue = state.value
        if (!stateValue.isAnswerLocked) return

        if (stateValue.currentIndex == stateValue.totalQuestions - 1) {
            finishRound()
            return
        }

        _state.update {
            it.copy(
                currentIndex = it.currentIndex + 1,
                selectedAnswer = null,
                isAnswerLocked = false,
                screenState = QuizScreenState.Playing
            )
        }
    }

    private fun observeProfile() {
        observeMushroomProfileUseCase()
            .onEach { profile ->
                _state.update { it.copy(profile = profile) }
            }.launchIn(viewModelScope)
    }

    private fun finishRound() = viewModelScope.launch {
        val stateValue = state.value
        val perfectBonus = if (stateValue.correctAnswers == stateValue.totalQuestions) 20 else 0
        val totalSpores = stateValue.sporesEarned + perfectBonus

        saveQuizResultUseCase(
            score = stateValue.correctAnswers,
            totalQuestions = stateValue.totalQuestions,
            earnedSpores = totalSpores,
            bestStreak = stateValue.bestStreak,
            masteredMushroomIds = stateValue.masteredMushroomIds
        )

        _state.update {
            it.copy(
                sporesEarned = totalSpores,
                screenState = QuizScreenState.Finished
            )
        }
    }

    private fun rewardFor(streak: Int) = 12 + streak * 3
}
