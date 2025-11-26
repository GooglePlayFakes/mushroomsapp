package ru.topbun.mushrooms.domain.useCases

import ru.topbun.mushrooms.data.repository.CategoryRepository
import ru.topbun.mushrooms.data.repository.FavoriteRepository

class GetCategoriesUseCase(
    private val repository: CategoryRepository
){

    suspend operator fun invoke() = repository.loadCategories()

}