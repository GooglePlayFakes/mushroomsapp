package ru.topbun.mushrooms.data.repository

import android.app.Application
import ru.topbun.mushrooms.data.local.AppDatabase
import ru.topbun.mushrooms.data.local.mapper.MushroomMapper
import ru.topbun.mushrooms.domain.entity.MushroomEntity
import ru.topbun.mushrooms.domain.entity.MushroomSortedType

class MushroomRepository(application: Application){

    private val database = AppDatabase.getInstance(application)
    private val mushroomDao = database.mushroomDao()
    private val mushroomMapper = MushroomMapper(application)

    suspend fun loadMushrooms(
        q: String,
        sortedType: MushroomSortedType,
        offset: Int,
        limit: Int
    ): List<MushroomEntity> {
        val mushrooms = mushroomDao.getMushrooms(q, sortedType, offset, limit)
        return mushroomMapper.toEntity(mushrooms)
    }

    suspend fun loadMushroom(id: Int): MushroomEntity? {
        val mushroom = mushroomDao.getMushroom(id)
        return mushroomMapper.toEntity(mushroom)
    }

}