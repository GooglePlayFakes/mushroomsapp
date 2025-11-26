package ru.topbun.mushrooms.presentation.screens.favorite

import ru.topbun.mushrooms.domain.entity.MushroomEntity

data class FavoriteState(
    val mushrooms: List<MushroomEntity> = emptyList(),
)