package ru.topbun.mushrooms.domain.useCases

import ru.topbun.mushrooms.data.repository.QuizRepository

class GetMasteredMushroomsUseCase(
    private val repository: QuizRepository
) {

    suspend operator fun invoke(ids: Set<Int>, limit: Int = 3) =
        repository.loadMasteredMushrooms(ids, limit)
}
