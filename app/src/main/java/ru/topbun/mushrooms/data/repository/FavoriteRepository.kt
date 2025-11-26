package ru.topbun.mushrooms.data.repository

import android.app.Application
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.topbun.mushrooms.data.local.AppDatabase
import ru.topbun.mushrooms.data.local.entity.FavoriteDbo
import ru.topbun.mushrooms.data.local.entity.MushroomDbo
import ru.topbun.mushrooms.data.local.mapper.MushroomMapper
import ru.topbun.mushrooms.domain.entity.MushroomEntity

class FavoriteRepository(application: Application){

    private val database = AppDatabase.getInstance(application)
    private val favoriteDao = database.favoriteDao()
    private val mushroomDao = database.mushroomDao()
    private val mushroomMapper = MushroomMapper(application)

    suspend fun loadFavoriteMushrooms() = favoriteDao.getFavoriteMushroomId().map {
        it.map { mushroomDao.getMushroom(it) }
    }.map { mushroomMapper.toEntity(it) }


    suspend fun switchFavorite(mushroomId: Int): Boolean{
        val favoriteStatus = favoriteDao.getFavoriteStatus(mushroomId) ?: false
        val newFavoriteStatus = !favoriteStatus
        val favorite = FavoriteDbo(mushroomId, newFavoriteStatus)
        favoriteDao.insertFavorite(favorite)
        return newFavoriteStatus
    }

}