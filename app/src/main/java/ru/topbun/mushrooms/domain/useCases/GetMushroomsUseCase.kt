package ru.topbun.mushrooms.domain.useCases

import ru.topbun.mushrooms.data.repository.MushroomRepository
import ru.topbun.mushrooms.domain.entity.MushroomSortedType

class GetMushroomsUseCase(
    private val repository: MushroomRepository
) {

    suspend operator fun invoke(
        q: String,
        sortedType: MushroomSortedType,
        offset: Int = 0,
        limit: Int = 20
    ) = repository.loadMushrooms(q, sortedType, offset, limit)

}