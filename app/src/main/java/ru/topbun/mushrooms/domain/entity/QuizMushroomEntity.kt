package ru.topbun.mushrooms.domain.entity

data class QuizMushroomEntity(
    val id: Int,
    val title: String,
    val latTitle: String,
    val preview: String,
    val popularity: Int,
    val category: CategoryEntity?
)
