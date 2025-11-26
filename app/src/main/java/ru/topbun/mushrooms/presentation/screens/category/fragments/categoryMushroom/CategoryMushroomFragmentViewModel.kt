package ru.topbun.mushrooms.presentation.screens.category.fragments.categoryMushroom

import android.R.attr.category
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import coil3.util.CoilUtils.result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.topbun.mushrooms.data.repository.CategoryRepository
import ru.topbun.mushrooms.data.repository.FavoriteRepository
import ru.topbun.mushrooms.domain.useCases.GetCategoriesUseCase
import ru.topbun.mushrooms.domain.useCases.GetCategoryByIdUseCase
import ru.topbun.mushrooms.domain.useCases.GetMushroomByCategoryUseCase
import ru.topbun.mushrooms.domain.useCases.SwitchFavoriteUseCase
import ru.topbun.mushrooms.presentation.screens.category.fragments.category.CategoryFragmentState
import ru.topbun.mushrooms.presentation.screens.category.fragments.categoryMushroom.CategoryMushroomFragmentState.CategoryScreenState
import ru.topbun.mushrooms.presentation.screens.favorite.FavoriteState
import ru.topbun.mushrooms.presentation.screens.main.MainState

class CategoryMushroomFragmentViewModel(
    private val categoryId: Int,
    application: Application
): AndroidViewModel(application) {

    private val categoryRepository = CategoryRepository(application)
    private val favoriteRepository = FavoriteRepository(application)

    private val getMushroomByCategoryUseCase = GetMushroomByCategoryUseCase(categoryRepository)
    private val getCategoryByIdUseCase = GetCategoryByIdUseCase(categoryRepository)
    private val switchFavoriteUseCase = SwitchFavoriteUseCase(favoriteRepository)

    private val _state = MutableStateFlow(CategoryMushroomFragmentState())
    val state = _state.asStateFlow()

    fun switchFavorite(id: Int) = viewModelScope.launch{
        val status = switchFavoriteUseCase(id)
        _state.update {
            val newMushrooms = it.mushrooms.toList().map { mushroom ->
                if (mushroom.id == id) mushroom.copy(isFavorite = status) else mushroom
            }
            it.copy(mushrooms = newMushrooms)
        }
    }

    fun loadMushrooms() = viewModelScope.launch {
        val stateValue = state.value
        val result = getMushroomByCategoryUseCase(
            id = categoryId,
            offset = stateValue.mushrooms.size
        )
        _state.update {
            it.copy(
                mushrooms = stateValue.mushrooms + result,
                isEndList = result.isEmpty(),
                screenState = CategoryScreenState.Success
            )
        }
    }

    companion object {

        fun Factory(categoryId: Int, application: Application): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                return CategoryMushroomFragmentViewModel(categoryId, application) as T
            }
        }
    }


    private fun loadCategory() = viewModelScope.launch {
        val category = getCategoryByIdUseCase(categoryId)
        _state.update { it.copy(category = category) }
    }

    init {
        loadCategory()
    }

}