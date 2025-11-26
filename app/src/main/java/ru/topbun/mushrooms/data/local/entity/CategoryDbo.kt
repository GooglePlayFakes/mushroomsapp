package ru.topbun.mushrooms.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull
import ru.topbun.mushrooms.domain.entity.CategoryEntity

@Entity("categories")
data class CategoryDbo(
    @PrimaryKey
    val id: Int?,
    val name: String,
    val icon: String
){

    fun toEntity() = CategoryEntity(id = id ?: -1, name = name, icon = icon)

}