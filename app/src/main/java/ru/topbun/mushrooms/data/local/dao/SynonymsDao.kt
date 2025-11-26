package ru.topbun.mushrooms.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import ru.topbun.mushrooms.data.local.entity.DescriptionDbo
import ru.topbun.mushrooms.data.local.entity.SynonymsDbo

@Dao
interface SynonymsDao {

    @Query("SELECT * FROM synonyms WHERE mushroom_id = :mushroomId")
    suspend fun getSynonyms(mushroomId: Int): List<SynonymsDbo>

}