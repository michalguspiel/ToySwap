package com.erdees.toyswap.model.localDatabase

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.erdees.toyswap.model.models.item.itemCategory.ItemCategory
import com.erdees.toyswap.model.models.item.itemCategory.ItemCategoryWithChildren


@Dao
interface ItemCategoryDao {

    @Query("SELECT * FROM item_category WHERE id  =:searchedId ")
    fun getItemCategory(searchedId: Int): ItemCategory

    @Transaction
    @Query("SELECT * FROM ITEM_CATEGORY WHERE id =:id")
    fun getCategoryChildren(id: Int) : ItemCategoryWithChildren


}