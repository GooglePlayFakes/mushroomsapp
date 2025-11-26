package ru.topbun.mushrooms.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("mushrooms")
data class MushroomDbo(
    @PrimaryKey val id: Int?,
    val popularity: Int,
    val title: String,
    @ColumnInfo("lat_title") val latTitle: String,
    val preview: String,
)