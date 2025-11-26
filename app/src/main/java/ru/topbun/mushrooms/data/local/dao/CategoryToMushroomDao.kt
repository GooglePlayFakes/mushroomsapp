package ru.topbun.mushrooms.data.local.dao

import androidx.room.Dao
import androidx.room.Query

@Dao
interface CategoryToMushroomDao {

    @Query("SELECT mushroom_id FROM category_to_mushroom WHERE category_id = :categoryId LIMIT :limit  OFFSET :offset")
    suspend fun getMushroomsIdByCategoryId(categoryId: Int, offset: Int, limit: Int): List<Int>

    @Query("SELECT category_id FROM category_to_mushroom WHERE mushroom_id = :mushroomId")
    suspend fun getCategoryIdByMushroomId(mushroomId: Int): List<Int>

}