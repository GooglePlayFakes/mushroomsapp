package ru.topbun.mushrooms.domain.useCases

import ru.topbun.mushrooms.data.repository.FavoriteRepository

class GetFavoriteMushroomsUseCase(
    private val repository: FavoriteRepository
){

    suspend operator fun invoke() = repository.loadFavoriteMushrooms()

}