package ru.topbun.mushrooms.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import ru.topbun.mushrooms.data.local.entity.CategoryDbo

@Dao
interface CategoryDao {

    @Query("SELECT * FROM categories")
    suspend fun getCategories(): List<CategoryDbo>

    @Query("SELECT * FROM categories WHERE id = :id")
    suspend fun getCategoryById(id: Int): CategoryDbo

}