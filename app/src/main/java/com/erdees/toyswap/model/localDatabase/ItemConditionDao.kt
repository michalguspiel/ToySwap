package com.erdees.toyswap.model.localDatabase

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ItemConditionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertItemCondition(itemCondition: ItemCondition)

    @Query("SELECT * FROM ItemCondition ORDER BY id")
    fun getItemConditions(): List<ItemCondition>

}