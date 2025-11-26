package ru.topbun.mushrooms.presentation.screens.category.fragments.category

import ru.topbun.mushrooms.domain.entity.CategoryEntity

data class CategoryFragmentState(
    val categories: List<CategoryEntity> = emptyList()
)
