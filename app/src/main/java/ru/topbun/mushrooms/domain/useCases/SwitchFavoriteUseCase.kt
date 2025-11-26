package ru.topbun.mushrooms.domain.useCases

import ru.topbun.mushrooms.data.repository.FavoriteRepository

class SwitchFavoriteUseCase(
    private val repository: FavoriteRepository
){

    suspend operator fun invoke(id: Int) = repository.switchFavorite(id)

}