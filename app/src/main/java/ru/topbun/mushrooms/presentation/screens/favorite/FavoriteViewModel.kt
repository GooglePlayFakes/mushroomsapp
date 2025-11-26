package ru.topbun.mushrooms.presentation.screens.favorite

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.topbun.mushrooms.data.repository.FavoriteRepository
import ru.topbun.mushrooms.domain.useCases.GetFavoriteMushroomsUseCase
import ru.topbun.mushrooms.domain.useCases.SwitchFavoriteUseCase

class FavoriteViewModel(application: Application): AndroidViewModel(application) {

    private val repository = FavoriteRepository(application)

    private val getFavoriteMushroomsUseCase = GetFavoriteMushroomsUseCase(repository)
    private val switchFavoriteUseCase = SwitchFavoriteUseCase(repository)

    private val _state = MutableStateFlow(FavoriteState())
    val state = _state.asStateFlow()

    init {
        loadMushrooms()
    }

    private fun loadMushrooms() = viewModelScope.launch {
        getFavoriteMushroomsUseCase().collect { mushrooms ->
            _state.update { it.copy(mushrooms = mushrooms) }
        }
    }

    fun switchFavorite(id: Int) = viewModelScope.launch{
        val status = switchFavoriteUseCase(id)
        _state.update {
            val newMushrooms = it.mushrooms.toList().map { mushroom ->
                if (mushroom.id == id) mushroom.copy(isFavorite = status) else mushroom
            }
            it.copy(mushrooms = newMushrooms)
        }
    }

}