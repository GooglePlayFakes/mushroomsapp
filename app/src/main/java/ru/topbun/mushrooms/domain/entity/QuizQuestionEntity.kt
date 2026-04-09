package ru.topbun.mushrooms.domain.entity

data class QuizQuestionEntity(
    val mushroomId: Int,
    val type: QuizQuestionType,
    val title: String,
    val subtitle: String,
    val preview: String,
    val options: List<String>,
    val correctAnswer: String,
    val explanation: String,
)
