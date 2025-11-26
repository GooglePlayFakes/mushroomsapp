package ru.topbun.mushrooms.presentation.screens.category.fragments.categoryMushroom

import ru.topbun.mushrooms.domain.entity.CategoryEntity
import ru.topbun.mushrooms.domain.entity.MushroomEntity
import ru.topbun.mushrooms.presentation.screens.main.MainState.MainScreenState

data class CategoryMushroomFragmentState(
    val category: CategoryEntity? = null,
    val mushrooms: List<MushroomEntity> = emptyList(),
    val isEndList: Boolean = false,
    val screenState: CategoryScreenState = CategoryScreenState.Initial,
){

    sealed interface CategoryScreenState{
        data object Initial: CategoryScreenState
        data object Loading: CategoryScreenState
        data object Success: CategoryScreenState
    }

}
