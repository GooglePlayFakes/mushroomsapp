package ru.topbun.mushrooms.presentation.screens.category.fragments.category

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.topbun.mushrooms.data.repository.CategoryRepository
import ru.topbun.mushrooms.domain.useCases.GetCategoriesUseCase
import ru.topbun.mushrooms.presentation.screens.favorite.FavoriteState

class CategoryFragmentViewModel(application: Application): AndroidViewModel(application) {

    private val repository = CategoryRepository(application)
    private val getCategoriesUseCase = GetCategoriesUseCase(repository)

    private val _state = MutableStateFlow(CategoryFragmentState())
    val state = _state.asStateFlow()

    private fun loadCategories() = viewModelScope.launch {
        val categories = getCategoriesUseCase()
        _state.value = _state.value.copy(categories = categories)
    }

    init {
        loadCategories()
    }

}