package ru.topbun.mushrooms.presentation.screens.profile

import ru.topbun.mushrooms.domain.entity.MushroomProfileEntity
import ru.topbun.mushrooms.domain.entity.QuizMushroomEntity

data class ProfileState(
    val profile: MushroomProfileEntity = MushroomProfileEntity(),
    val favoriteCount: Int = 0,
    val masteredMushrooms: List<QuizMushroomEntity> = emptyList(),
    val screenState: ProfileScreenState = ProfileScreenState.Loading,
) {

    sealed interface ProfileScreenState {
        data object Loading : ProfileScreenState
        data object Success : ProfileScreenState
    }
}
