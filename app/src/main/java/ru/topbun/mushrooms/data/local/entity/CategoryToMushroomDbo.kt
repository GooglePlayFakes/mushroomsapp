package ru.topbun.mushrooms.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "category_to_mushroom",
    foreignKeys = [
        ForeignKey(
            entity = MushroomDbo::class,
            parentColumns = ["id"],
            childColumns = ["mushroom_id"],
            onDelete = ForeignKey.RESTRICT,
            onUpdate = ForeignKey.RESTRICT
        ),
        ForeignKey(
            entity = CategoryDbo::class,
            parentColumns = ["id"],
            childColumns = ["category_id"],
            onDelete = ForeignKey.RESTRICT,
            onUpdate = ForeignKey.RESTRICT
        ),
    ],
)
data class CategoryToMushroomDbo(

    @PrimaryKey val id: Int?,

    @ColumnInfo("mushroom_id")
    val mushroomId: Int,

    @ColumnInfo("category_id")
    val categoryId: Int
)