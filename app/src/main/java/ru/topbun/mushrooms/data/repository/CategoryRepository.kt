package ru.topbun.mushrooms.data.repository

import android.app.Application
import ru.topbun.mushrooms.data.local.AppDatabase
import ru.topbun.mushrooms.data.local.mapper.MushroomMapper
import ru.topbun.mushrooms.domain.entity.MushroomEntity

class CategoryRepository(application: Application){

    private val database = AppDatabase.getInstance(application)
    private val categoryDao = database.categoryDao()
    private val categoryToMushroomDao = database.categoryToMushroomDao()
    private val mushroomDao = database.mushroomDao()
    private val mushroomMapper = MushroomMapper(application)

    suspend fun getCategoryById(id: Int) = categoryDao.getCategoryById(id).toEntity()

    suspend fun loadCategories() = categoryDao.getCategories().map { it.toEntity() }

    suspend fun loadMushrooms(categoryId: Int, offset: Int, limit: Int): List<MushroomEntity> {
        val mushrooms = categoryToMushroomDao.getMushroomsIdByCategoryId(categoryId, offset, limit)
            .map { mushroomDao.getMushroom(it) }
        return mushroomMapper.toEntity(mushrooms)
    }

}