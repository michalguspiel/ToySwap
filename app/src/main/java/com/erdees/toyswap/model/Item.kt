package com.erdees.toyswap.model

data class Item(
    val name: String = "",
    val category: ItemCategory ,
    val description: String = "",
    val price: String = "",
    val pictureUrl: String = "",
    val userId: String = "",


    ) {
}