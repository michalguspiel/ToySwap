package com.erdees.toyswap.model.models.item.itemCategory

import androidx.room.Embedded
import androidx.room.Relation


data class ItemCategoryWithChildren(
    @Embedded val itemCategory: ItemCategory,
    @Relation(
        parentColumn = "id",
        entityColumn = "parentId"
    ) val itemCategoryChildren : List<ItemCategory>
)