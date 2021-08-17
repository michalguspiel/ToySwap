package com.erdees.toyswap.model.localDatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.erdees.toyswap.model.models.item.itemCategory.ItemCategory
import com.erdees.toyswap.model.models.item.itemCondition.ItemCondition

/**
 * DATABASE WHERE ITEM CONDITIONS AND ITEM CATEGORIES ARE STORED, PURPOSE OF THIS IS TO NOT HAVE HARD CODED OBJECTS INSIDE APP.
 * */
@Database(entities = [(ItemCondition::class),(ItemCategory::class),], version = 1)
abstract class LocalDatabase : RoomDatabase() {

    abstract fun itemConditionDao(): ItemConditionDao

    abstract fun itemCategoryDao() : ItemCategoryDao


    companion object {
        @Volatile
        private var INSTANCE: LocalDatabase? = null

        fun getDatabase(context: Context): LocalDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    LocalDatabase::class.java,
                    "local_database"
                ).addMigrations()
                    .allowMainThreadQueries()
                    .createFromAsset("database/toyswap.db")
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }


}
