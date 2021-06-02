package com.erdees.toyswap.model

open class ItemCategory(
    val categoryName : String,
    val parent : ItemCategory?,
    val children : List<ItemCategory>? = null,
) {
}


