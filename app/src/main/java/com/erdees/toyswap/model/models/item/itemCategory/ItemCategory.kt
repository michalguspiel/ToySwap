package com.erdees.toyswap.model.models.item.itemCategory

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.erdees.toyswap.DrawableStaticMap


@Entity(tableName = "item_category")
data class ItemCategory(
    @PrimaryKey val id: Int = 0,
    val parentId : Int? = null,
    val name: String= "",
    val iconRef: String? = null
) {
    @Ignore
    val icon: Int? = DrawableStaticMap.map[iconRef]
}
