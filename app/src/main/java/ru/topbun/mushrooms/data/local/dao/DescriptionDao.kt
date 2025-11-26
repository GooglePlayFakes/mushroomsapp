package ru.topbun.mushrooms.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import ru.topbun.mushrooms.data.local.entity.DescriptionDbo

@Dao
interface DescriptionDao {

    @Query("SELECT * FROM descriptions WHERE mushroom_id = :mushroomId")
    suspend fun getDescriptions(mushroomId: Int): List<DescriptionDbo>

}