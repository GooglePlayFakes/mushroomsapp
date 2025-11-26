package ru.topbun.mushrooms.domain.useCases

import ru.topbun.mushrooms.data.repository.CategoryRepository

class GetCategoryByIdUseCase(
    private val categoryRepository: CategoryRepository
) {

    suspend operator fun invoke(id: Int) = categoryRepository.getCategoryById(id)

}