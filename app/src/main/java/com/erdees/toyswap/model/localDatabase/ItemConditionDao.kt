package com.erdees.toyswap.model.localDatabase

import androidx.room.Dao
import androidx.room.Query

@Dao
interface ItemConditionDao {

    @Query("SELECT * FROM item_conditions ORDER BY id")
    fun getItemConditions(): List<ItemCondition>

}