package com.erdees.toyswap.model.models

open class ItemCategory (
    val categoryName : String = "",
    val parent : ItemCategory? = null,
    val children : List<ItemCategory>? = null,
) {
}


