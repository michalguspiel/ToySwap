package com.erdees.toyswap.model.localDatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.erdees.toyswap.R
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader

/**
 * DATABASE WHERE ITEM CONDITIONS AND ITEM CATEGORIES ARE STORED, PURPOSE OF THIS IS TO NOT HAVE HARD CODED OBJECTS INSIDE APP.
 * */
@Database(entities = [ItemCondition::class],version = 1)
abstract class LocalDatabase : RoomDatabase() {

    abstract fun itemConditionDao() : ItemConditionDao


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
                ).allowMainThreadQueries()
                    .addCallback(object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                          //  prepopulateDatabaseWithJsonData(context)
                        }
                    })
                    .build()
                INSTANCE = instance
                return instance
            }
        }
        }


         fun prepopulateDatabaseWithJsonData(context : Context){
            val itemConditions = loadJsonArray(context)

            try {
                if (itemConditions != null) {
                    for(i in 0 until itemConditions.length()) {
                        val itemCondition = itemConditions.getJSONObject(i)
                        val id = itemCondition.getString("id").toLong()
                        val name = itemCondition.getString("itemCondition")
                        INSTANCE?.itemConditionDao()?.insertItemCondition(ItemCondition(id, name))
                    }

                }
            }catch (e : Exception){
                e.printStackTrace()
            }

        }

        private fun loadJsonArray(context: Context):JSONArray? {
            val builder : StringBuilder = StringBuilder()
            val input : InputStream = context.resources.openRawResource(R.raw.local_database)
            val reader  = BufferedReader(InputStreamReader(input))
            var line : String
            try {
                line = reader.readLine()
                while (line  != null){
                    builder.append(line)
                }
                val json  = JSONObject(builder.toString())
                return json.getJSONArray("itemConditions")
            }catch (exception: IOException) {
                exception.printStackTrace()
            }
            return null
        }


    }
