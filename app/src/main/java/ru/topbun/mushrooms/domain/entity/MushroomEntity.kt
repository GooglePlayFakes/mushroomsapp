package ru.topbun.mushrooms.domain.entity

data class MushroomEntity(
    val id: Int,
    val popularity: Int,
    val title: String,
    val latTitle: String,
    val description: List<DescriptionEntity>,
    val preview: String,
    val categories: List<CategoryEntity>,
    val synonyms: List<String>,
    val other: List<OtherEntity>,
    val isFavorite: Boolean
)