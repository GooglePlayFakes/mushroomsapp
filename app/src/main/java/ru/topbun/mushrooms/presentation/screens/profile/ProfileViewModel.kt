package ru.topbun.mushrooms.presentation.screens.profile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.topbun.mushrooms.data.repository.FavoriteRepository
import ru.topbun.mushrooms.data.repository.MushroomProfileRepository
import ru.topbun.mushrooms.data.repository.QuizRepository
import ru.topbun.mushrooms.domain.useCases.GetFavoriteMushroomsUseCase
import ru.topbun.mushrooms.domain.useCases.GetMasteredMushroomsUseCase
import ru.topbun.mushrooms.domain.useCases.ObserveMushroomProfileUseCase
import ru.topbun.mushrooms.presentation.screens.profile.ProfileState.ProfileScreenState

class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    private val favoriteRepository = FavoriteRepository(application)
    private val quizRepository = QuizRepository(application)
    private val profileRepository = MushroomProfileRepository(application)

    private val getFavoriteMushroomsUseCase = GetFavoriteMushroomsUseCase(favoriteRepository)
    private val getMasteredMushroomsUseCase = GetMasteredMushroomsUseCase(quizRepository)
    private val observeMushroomProfileUseCase = ObserveMushroomProfileUseCase(profileRepository)

    private val _state = MutableStateFlow(ProfileState())
    val state = _state.asStateFlow()

    init {
        observeProfile()
        observeFavorites()
    }

    private fun observeProfile() {
        observeMushroomProfileUseCase()
            .onEach { profile ->
                val masteredMushrooms = getMasteredMushroomsUseCase(profile.masteredMushroomIds)
                _state.update {
                    it.copy(
                        profile = profile,
                        masteredMushrooms = masteredMushrooms,
                        screenState = ProfileScreenState.Success
                    )
                }
            }.launchIn(viewModelScope)
    }

    private fun observeFavorites() = viewModelScope.launch {
        getFavoriteMushroomsUseCase().collect { mushrooms ->
            _state.update {
                it.copy(
                    favoriteCount = mushrooms.size,
                    screenState = ProfileScreenState.Success
                )
            }
        }
    }
}
