package ru.topbun.mushrooms.domain.useCases

import ru.topbun.mushrooms.data.repository.MushroomProfileRepository

class SaveQuizResultUseCase(
    private val repository: MushroomProfileRepository
) {

    suspend operator fun invoke(
        score: Int,
        totalQuestions: Int,
        earnedSpores: Int,
        bestStreak: Int,
        masteredMushroomIds: Set<Int>,
    ) = repository.saveQuizResult(
        score = score,
        totalQuestions = totalQuestions,
        earnedSpores = earnedSpores,
        bestStreak = bestStreak,
        masteredMushroomIds = masteredMushroomIds
    )
}
