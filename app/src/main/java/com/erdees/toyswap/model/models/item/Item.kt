package com.erdees.toyswap.model.models.item

import com.google.firebase.Timestamp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

data class Item(
    val name: String = "",
    val category: ItemCategoryFirebase = ItemCategoryFirebase("",null,
        Firebase.firestore.document("")),
    val description: String = "",
    val size : String = "",
    val itemCondition: String = "",
    val price: Double = 0.0,
    val deliveryCost: Double = 0.0,
    val mainImageUrl: String = "",
    val otherImagesUrl: List<String> = listOf(),
    val isItemAvailable: Boolean = true,
    val timeStamp: Timestamp = Timestamp.now(),
    val userId: String = "",
) {

}