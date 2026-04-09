package ru.topbun.mushrooms.data.repository

import android.app.Application
import kotlin.random.Random
import ru.topbun.mushrooms.data.local.AppDatabase
import ru.topbun.mushrooms.data.local.entity.MushroomDbo
import ru.topbun.mushrooms.domain.entity.CategoryEntity
import ru.topbun.mushrooms.domain.entity.MushroomSortedType
import ru.topbun.mushrooms.domain.entity.QuizMushroomEntity
import ru.topbun.mushrooms.domain.entity.QuizQuestionEntity
import ru.topbun.mushrooms.domain.entity.QuizQuestionType

class QuizRepository(application: Application) {

    private val database = AppDatabase.getInstance(application)
    private val mushroomDao = database.mushroomDao()
    private val categoryDao = database.categoryDao()
    private val categoryToMushroomDao = database.categoryToMushroomDao()

    suspend fun createQuiz(count: Int = 5): List<QuizQuestionEntity> {
        val random = Random(System.currentTimeMillis())
        val pool = loadQuizPool(limit = maxOf(count * 10, 48))
        if (pool.size < 4) return emptyList()

        val categories = categoryDao.getCategories().map { it.toEntity() }
        val questions = mutableListOf<QuizQuestionEntity>()
        val mushrooms = pool.shuffled(random).take(count)
        val types = listOf(
            QuizQuestionType.ByPhoto,
            QuizQuestionType.ByLatinTitle,
            QuizQuestionType.ByCategory
        )

        mushrooms.forEachIndexed { index, mushroom ->
            val preferredType = types[index % types.size]
            questions += createQuestion(
                mushroom = mushroom,
                preferredType = preferredType,
                pool = pool,
                categories = categories,
                random = random
            )
        }

        return questions
    }

    suspend fun loadMasteredMushrooms(
        ids: Set<Int>,
        limit: Int = 3
    ): List<QuizMushroomEntity> {
        if (ids.isEmpty()) return emptyList()
        return ids.mapNotNull { id ->
            runCatching { mushroomDao.getMushroom(id) }.getOrNull()
        }.mapNotNull { toQuizEntity(it) }
            .sortedByDescending { it.popularity }
            .take(limit)
    }

    private suspend fun createQuestion(
        mushroom: QuizMushroomEntity,
        preferredType: QuizQuestionType,
        pool: List<QuizMushroomEntity>,
        categories: List<CategoryEntity>,
        random: Random,
    ): QuizQuestionEntity {
        return when {
            preferredType == QuizQuestionType.ByLatinTitle && mushroom.latTitle.isNotBlank() ->
                createLatinQuestion(mushroom, pool, random)

            preferredType == QuizQuestionType.ByCategory && mushroom.category != null ->
                createCategoryQuestion(mushroom, categories, random)

            else -> createPhotoQuestion(mushroom, pool, random)
        }
    }

    private fun createPhotoQuestion(
        mushroom: QuizMushroomEntity,
        pool: List<QuizMushroomEntity>,
        random: Random,
    ): QuizQuestionEntity {
        val options = buildOptions(
            correct = mushroom.title,
            source = pool.map { it.title },
            random = random
        )

        return QuizQuestionEntity(
            mushroomId = mushroom.id,
            type = QuizQuestionType.ByPhoto,
            title = "Что за гриб на фото?",
            subtitle = "Выбери правильное название",
            preview = mushroom.preview,
            options = options,
            correctAnswer = mushroom.title,
            explanation = buildString {
                append("Это ${mushroom.title}.")
                mushroom.category?.name?.let { append(" Категория: $it.") }
            }
        )
    }

    private fun createLatinQuestion(
        mushroom: QuizMushroomEntity,
        pool: List<QuizMushroomEntity>,
        random: Random,
    ): QuizQuestionEntity {
        val latinPool = pool.map { it.latTitle }.filter { it.isNotBlank() }
        val options = buildOptions(
            correct = mushroom.latTitle,
            source = latinPool,
            random = random
        )

        return QuizQuestionEntity(
            mushroomId = mushroom.id,
            type = QuizQuestionType.ByLatinTitle,
            title = "Какое латинское имя у этого гриба?",
            subtitle = mushroom.title,
            preview = mushroom.preview,
            options = options,
            correctAnswer = mushroom.latTitle,
            explanation = "${mushroom.title} в атласе указан как ${mushroom.latTitle}."
        )
    }

    private fun createCategoryQuestion(
        mushroom: QuizMushroomEntity,
        categories: List<CategoryEntity>,
        random: Random,
    ): QuizQuestionEntity {
        val correctCategory = mushroom.category?.name.orEmpty()
        val options = buildOptions(
            correct = correctCategory,
            source = categories.map { it.name },
            random = random
        )

        return QuizQuestionEntity(
            mushroomId = mushroom.id,
            type = QuizQuestionType.ByCategory,
            title = "К какой категории относится гриб?",
            subtitle = mushroom.title,
            preview = mushroom.preview,
            options = options,
            correctAnswer = correctCategory,
            explanation = "${mushroom.title} относится к категории «$correctCategory»."
        )
    }

    private fun buildOptions(
        correct: String,
        source: List<String>,
        random: Random,
    ): List<String> {
        val otherOptions = source.distinct()
            .filter { it != correct }
            .shuffled(random)
            .take(3)
        return (otherOptions + correct).shuffled(random)
    }

    private suspend fun loadQuizPool(limit: Int): List<QuizMushroomEntity> {
        return mushroomDao.getMushrooms(
            q = "",
            type = MushroomSortedType.Popularity,
            offset = 0,
            limit = limit
        ).mapNotNull { toQuizEntity(it) }
    }

    private suspend fun toQuizEntity(mushroom: MushroomDbo): QuizMushroomEntity? {
        val mushroomId = mushroom.id ?: return null
        val categoryId = categoryToMushroomDao.getCategoryIdByMushroomId(mushroomId).firstOrNull()
        val category = categoryId?.let { categoryDao.getCategoryById(it).toEntity() }
        return QuizMushroomEntity(
            id = mushroomId,
            title = mushroom.title,
            latTitle = mushroom.latTitle,
            preview = mushroom.preview,
            popularity = mushroom.popularity,
            category = category
        )
    }
}
