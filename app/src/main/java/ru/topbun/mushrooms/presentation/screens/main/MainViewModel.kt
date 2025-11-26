package ru.topbun.mushrooms.presentation.screens.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.topbun.mushrooms.data.repository.FavoriteRepository
import ru.topbun.mushrooms.data.repository.MushroomRepository
import ru.topbun.mushrooms.domain.useCases.GetMushroomsUseCase
import ru.topbun.mushrooms.domain.useCases.SwitchFavoriteUseCase

class MainViewModel(application: Application): AndroidViewModel(application) {

    private val mushroomRepository = MushroomRepository(application)
    private val favoriteRepository = FavoriteRepository(application)

    private val getMushroomsUseCase = GetMushroomsUseCase(mushroomRepository)
    private val switchFavoriteUseCase = SwitchFavoriteUseCase(favoriteRepository)

    private val _state = MutableStateFlow(MainState())
    val state = _state.asStateFlow()


    fun changeQuery(value: String) = _state.update { it.copy(query = value) }
    fun changeTabSelected(index: Int) = _state.update { it.copy(selectedIndex = index) }

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
        val result = getMushroomsUseCase(
            q = stateValue.query,
            sortedType = stateValue.tabs[stateValue.selectedIndex],
            offset = stateValue.mushrooms.size
        )
        _state.update {
            it.copy(
                mushrooms = stateValue.mushrooms + result,
                isEndList = result.isEmpty(),
                screenState = MainState.MainScreenState.Success
            )
        }
    }


    init {
        handleChangeState()
    }

    private fun handleChangeState(){
        _state
            .map { it.query to it.selectedIndex }
            .distinctUntilChanged()
            .onEach {
                _state.update { it.copy(mushrooms = emptyList(), screenState = MainState.MainScreenState.Initial, isEndList = false) }
            }.launchIn(viewModelScope)
    }
}