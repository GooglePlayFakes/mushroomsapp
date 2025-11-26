package ru.topbun.mushrooms.domain.useCases

import ru.topbun.mushrooms.data.repository.CategoryRepository

class GetMushroomByCategoryUseCase(
    private val repository: CategoryRepository
) {

    suspend operator fun invoke(id: Int, offset: Int = 0, limit: Int = 20) =
        repository.loadMushrooms(id, offset, limit)

}