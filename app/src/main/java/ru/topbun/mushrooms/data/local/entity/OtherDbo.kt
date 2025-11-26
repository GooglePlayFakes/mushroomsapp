package ru.topbun.mushrooms.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import ru.topbun.mushrooms.domain.entity.OtherEntity

@Entity(
    tableName = "others",
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
data class OtherDbo(
    @PrimaryKey val id: Int?,
    @ColumnInfo("mushroom_id") val mushroomId: Int,
    val title: String,
    val value: String
){

    fun toEntity() = OtherEntity(title, value)

}
