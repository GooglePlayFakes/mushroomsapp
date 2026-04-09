package ru.topbun.mushrooms.domain.useCases

import ru.topbun.mushrooms.data.repository.QuizRepository

class CreateQuizRoundUseCase(
    private val repository: QuizRepository
) {

    suspend operator fun invoke(count: Int = 5) = repository.createQuiz(count)
}
