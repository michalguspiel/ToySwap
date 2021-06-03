package com.erdees.toyswap.model.models

import com.google.firebase.Timestamp

data class Item(
    val name: String = "",
    val category: ItemCategory,
    val description: String = "",
    val price: String = "",
    val mainImageUrl: String = "",
    val otherImagesUrl: List<String> = listOf(),
    val timeStamp : Timestamp,
    val userId: String = "",
    ) {
}