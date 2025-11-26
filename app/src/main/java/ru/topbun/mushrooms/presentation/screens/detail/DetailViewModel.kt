package ru.topbun.mushrooms.presentation.screens.detail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.topbun.mushrooms.data.repository.FavoriteRepository
import ru.topbun.mushrooms.data.repository.MushroomRepository
import ru.topbun.mushrooms.domain.useCases.GetMushroomByIdUseCase
import ru.topbun.mushrooms.domain.useCases.SwitchFavoriteUseCase

class DetailViewModel(
    private val mushroomId: Int,
    application: Application,
): AndroidViewModel(application) {

    private val mushroomRepository = MushroomRepository(application)
    private val getMushroomByIdUseCase = GetMushroomByIdUseCase(mushroomRepository)

    private val favoriteRepository = FavoriteRepository(application)
    private val switchFavoriteUseCase = SwitchFavoriteUseCase(favoriteRepository)

    private val _state = MutableStateFlow(DetailState())
    val state = _state.asStateFlow()

    private fun loadMushroom() = viewModelScope.launch {
        val mushroom = getMushroomByIdUseCase(mushroomId)
        _state.update { it.copy(mushroom = mushroom) }
    }

    fun switchFavorite() = viewModelScope.launch{
        val status = switchFavoriteUseCase(mushroomId)
        _state.update {
            val newMushroom = it.mushroom?.copy(isFavorite = status) ?: return@launch
            it.copy(mushroom = newMushroom)
        }
    }

    init {
        loadMushroom()
    }


    companion object {

        fun Factory(mushroomId: Int, application: Application): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                return DetailViewModel(mushroomId, application) as T
            }
        }
    }

}