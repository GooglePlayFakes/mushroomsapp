package ru.topbun.mushrooms.data.local.dao

import androidx.compose.ui.geometry.Offset
import androidx.room.Dao
import androidx.room.Query
import ru.topbun.mushrooms.data.local.entity.CategoryDbo
import ru.topbun.mushrooms.data.local.entity.MushroomDbo
import ru.topbun.mushrooms.domain.entity.MushroomEntity
import ru.topbun.mushrooms.domain.entity.MushroomSortedType

@Dao
interface MushroomDao {

    @Query("""
        SELECT *
        FROM mushrooms
        WHERE title LIKE '%' || :q || '%'
        ORDER BY
            CASE WHEN :type = 'Alphabet' THEN title END ASC,
            CASE WHEN :type = 'Popularity' THEN popularity END DESC
        LIMIT :limit OFFSET :offset
    """)
    suspend fun getMushrooms(q: String, type: MushroomSortedType, offset: Int, limit: Int): List<MushroomDbo>

    @Query("SELECT * FROM mushrooms WHERE id = :id")
    suspend fun getMushroom(id: Int): MushroomDbo

}