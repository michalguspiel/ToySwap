package com.erdees.toyswap.model.localDatabase

import androidx.room.Dao
import androidx.room.Query
import com.erdees.toyswap.model.models.item.itemCategory.ItemCategoryCrossRef


@Dao
interface ItemCategoryCrossRefDao {

@Query("SELECT * FROM item_category_cross_ref WHERE parentId =:id")
fun getCategoryChildren(id: Int) : List<ItemCategoryCrossRef>

@Query("SELECT * FROM item_category_cross_ref WHERE childId =:id")
fun getCategoryParent(id: Int) : List<ItemCategoryCrossRef>


}