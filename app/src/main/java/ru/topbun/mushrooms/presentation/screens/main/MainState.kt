package ru.topbun.mushrooms.presentation.screens.main

import ru.topbun.mushrooms.domain.entity.MushroomEntity
import ru.topbun.mushrooms.domain.entity.MushroomSortedType

data class MainState(
    val query: String = "",
    val tabs: List<MushroomSortedType> = MushroomSortedType.entries,
    val selectedIndex: Int = 0,
    val mushrooms: List<MushroomEntity> = emptyList(),
    val isEndList: Boolean = false,
    val screenState: MainScreenState = MainScreenState.Initial,
){

    sealed interface MainScreenState{
        data object Initial: MainScreenState
        data object Loading: MainScreenState
        data object Success: MainScreenState
    }

}