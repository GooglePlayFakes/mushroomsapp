package ru.topbun.mushrooms.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "synonyms",
    foreignKeys = [
        ForeignKey(
            entity = MushroomDbo::class,
            parentColumns = ["id"],
            childColumns = ["mushroom_id"],
            onDelete = ForeignKey.RESTRICT,
            onUpdate = ForeignKey.RESTRICT
        )
    ]
)
data class SynonymsDbo(
    @PrimaryKey val id: Int?,
    @ColumnInfo("mushroom_id") val mushroomId: Int,
    val name: String,
)