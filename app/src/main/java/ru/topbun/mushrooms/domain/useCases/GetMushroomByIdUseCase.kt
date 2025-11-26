package ru.topbun.mushrooms.domain.useCases

import ru.topbun.mushrooms.data.repository.MushroomRepository

class GetMushroomByIdUseCase(
    private val repository: MushroomRepository
) {

    suspend operator fun invoke(id: Int) = repository.loadMushroom(id)

}