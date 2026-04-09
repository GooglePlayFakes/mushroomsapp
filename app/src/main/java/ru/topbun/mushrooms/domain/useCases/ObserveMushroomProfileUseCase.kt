package ru.topbun.mushrooms.domain.useCases

import ru.topbun.mushrooms.data.repository.MushroomProfileRepository

class ObserveMushroomProfileUseCase(
    private val repository: MushroomProfileRepository
) {

    operator fun invoke() = repository.observeProfile()
}
