package ru.topbun.mushrooms.domain.entity

data class MushroomProfileEntity(
    val spores: Int = 0,
    val quizzesCompleted: Int = 0,
    val correctAnswers: Int = 0,
    val answeredQuestions: Int = 0,
    val bestScore: Int = 0,
    val bestStreak: Int = 0,
    val perfectRounds: Int = 0,
    val masteredMushroomIds: Set<Int> = emptySet()
)
