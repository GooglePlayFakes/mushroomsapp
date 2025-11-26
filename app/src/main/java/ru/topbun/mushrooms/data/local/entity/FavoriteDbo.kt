package ru.topbun.mushrooms.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "favorites",
    foreignKeys = [
        ForeignKey(
            entity = MushroomDbo::class,
            parentColumns = ["id"],
            childColumns = ["mushroom_id"],
        )
    ]
)
data class FavoriteDbo(
    @PrimaryKey
    @ColumnInfo("mushroom_id")
    val mushroomId: Int,
    val status: Boolean
)