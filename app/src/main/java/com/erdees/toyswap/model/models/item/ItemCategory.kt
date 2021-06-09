package com.erdees.toyswap.model.models.item

import android.graphics.drawable.Icon

open class ItemCategory (
    val categoryName : String = "",
    val parent : ItemCategory? = null,
    val children : List<ItemCategory>? = null,
    val icon: Icon? = null
) {
}


