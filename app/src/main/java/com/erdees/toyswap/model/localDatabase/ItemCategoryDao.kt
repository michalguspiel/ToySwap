package com.erdees.toyswap.model.localDatabase

import androidx.room.Dao
import androidx.room.Query
import com.erdees.toyswap.model.models.item.itemCategory.ItemCategory


@Dao
interface ItemCategoryDao {

    @Query("SELECT * FROM item_category WHERE id  =:searchedId ")
    fun getItemCategory(searchedId: Int): ItemCategory


}