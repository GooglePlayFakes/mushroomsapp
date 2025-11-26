package ru.topbun.mushrooms.data.local.mapper

import android.R.attr.description
import android.app.Application
import ru.topbun.mushrooms.data.local.AppDatabase
import ru.topbun.mushrooms.data.local.entity.MushroomDbo
import ru.topbun.mushrooms.domain.entity.MushroomEntity

class MushroomMapper(application: Application) {

    private val database = AppDatabase.getInstance(application)
    private val descriptionDao = database.descriptionDao()
    private val categoryDao = database.categoryDao()
    private val categoryToMushroomDao = database.categoryToMushroomDao()
    private val synonymsDao = database.synonymsDao()
    private val otherDao = database.otherDao()
    private val favoriteDao = database.favoriteDao()

    suspend fun toEntity(mushroom: MushroomDbo): MushroomEntity? {
        mushroom.id ?: return null
        val descriptions = descriptionDao.getDescriptions(mushroom.id).map {it.toEntity()}
        val categories = categoryToMushroomDao.getCategoryIdByMushroomId(mushroom.id).map {
            categoryDao.getCategoryById(it).toEntity()
        }
        val synonyms = synonymsDao.getSynonyms(mushroom.id).map { it.name }
        val others = otherDao.getOthers(mushroom.id).map { it.toEntity() }
        val isFavorite = favoriteDao.getFavoriteStatus(mushroom.id) ?: false

        return MushroomEntity(
            id = mushroom.id,
            popularity = mushroom.popularity,
            title = mushroom.title,
            latTitle = mushroom.latTitle,
            description = descriptions,
            preview = mushroom.preview,
            categories = categories,
            synonyms = synonyms,
            other = others,
            isFavorite = isFavorite
        )
    }

    suspend fun toEntity(mushrooms: List<MushroomDbo>) = mushrooms.mapNotNull { toEntity(it) }

}