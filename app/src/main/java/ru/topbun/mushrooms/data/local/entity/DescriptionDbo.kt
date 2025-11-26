package ru.topbun.mushrooms.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import ru.topbun.mushrooms.domain.entity.DescriptionEntity
import ru.topbun.mushrooms.domain.entity.DescriptionType

@Entity(
    tableName = "descriptions",
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
data class DescriptionDbo(
    @PrimaryKey val id: Int?,
    @ColumnInfo("mushroom_id") val mushroomId: Int,
    val content: String,
    val type: Int
){

    fun toEntity() = DescriptionEntity(
        content = content,
        type = DescriptionType.entries[type]
    )

}
