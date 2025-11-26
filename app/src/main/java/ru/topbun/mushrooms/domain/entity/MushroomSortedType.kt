package ru.topbun.mushrooms.domain.entity

enum class MushroomSortedType {

    Popularity, Alphabet;

    fun toUiString() = when(this){
        Popularity -> "По популярности"
        Alphabet -> "По алфавиту"
    }

}