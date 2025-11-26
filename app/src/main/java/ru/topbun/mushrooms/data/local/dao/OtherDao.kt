package ru.topbun.mushrooms.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import ru.topbun.mushrooms.data.local.entity.DescriptionDbo
import ru.topbun.mushrooms.data.local.entity.OtherDbo
import ru.topbun.mushrooms.domain.entity.OtherEntity

@Dao
interface OtherDao {

    @Query("SELECT * FROM `others` WHERE mushroom_id = :mushroomId")
    suspend fun getOthers(mushroomId: Int): List<OtherDbo>

}