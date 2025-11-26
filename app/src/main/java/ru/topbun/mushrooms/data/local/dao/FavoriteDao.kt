package ru.topbun.mushrooms.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.topbun.mushrooms.data.local.entity.CategoryDbo
import ru.topbun.mushrooms.data.local.entity.FavoriteDbo
import ru.topbun.mushrooms.data.local.entity.MushroomDbo

@Dao
interface FavoriteDao {


    @Query("SELECT mushroom_id FROM favorites WHERE status = 1")
    fun getFavoriteMushroomId(): Flow<List<Int>>

    @Query("SELECT status FROM favorites WHERE mushroom_id = :mushroomId LIMIT 1")
    suspend fun getFavoriteStatus(mushroomId: Int): Boolean?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(favorite: FavoriteDbo)

}