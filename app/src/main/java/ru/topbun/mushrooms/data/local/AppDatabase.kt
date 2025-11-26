package ru.topbun.mushrooms.data.local

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.topbun.mushrooms.data.local.dao.CategoryDao
import ru.topbun.mushrooms.data.local.dao.CategoryToMushroomDao
import ru.topbun.mushrooms.data.local.dao.DescriptionDao
import ru.topbun.mushrooms.data.local.dao.FavoriteDao
import ru.topbun.mushrooms.data.local.dao.MushroomDao
import ru.topbun.mushrooms.data.local.dao.OtherDao
import ru.topbun.mushrooms.data.local.dao.SynonymsDao
import ru.topbun.mushrooms.data.local.entity.CategoryDbo
import ru.topbun.mushrooms.data.local.entity.CategoryToMushroomDbo
import ru.topbun.mushrooms.data.local.entity.DescriptionDbo
import ru.topbun.mushrooms.data.local.entity.FavoriteDbo
import ru.topbun.mushrooms.data.local.entity.MushroomDbo
import ru.topbun.mushrooms.data.local.entity.OtherDbo
import ru.topbun.mushrooms.data.local.entity.SynonymsDbo

@Database(
    entities = [
        CategoryDbo::class,
        CategoryToMushroomDbo::class,
        DescriptionDbo::class,
        MushroomDbo::class,
        OtherDbo::class,
        SynonymsDbo::class,
        FavoriteDbo::class
    ],
    version = 1
)
abstract class AppDatabase: RoomDatabase() {

    abstract fun categoryDao(): CategoryDao
    abstract fun categoryToMushroomDao(): CategoryToMushroomDao
    abstract fun descriptionDao(): DescriptionDao
    abstract fun mushroomDao(): MushroomDao
    abstract fun otherDao(): OtherDao
    abstract fun synonymsDao(): SynonymsDao
    abstract fun favoriteDao(): FavoriteDao

    companion object{

        private const val DB_NAME = "mushrooms.db"
        private const val DUMP_PATH = "database/mushrooms.db"
        private var INSTANCE: AppDatabase? = null

        fun getInstance(application: Application) = INSTANCE ?: synchronized(this) {
            INSTANCE ?: createDatabase(application).also { INSTANCE = it }
        }
        
        private fun createDatabase(application: Application) = Room.databaseBuilder(
            application, AppDatabase::class.java, DB_NAME
        ).createFromAsset(DUMP_PATH).build()

    }

}