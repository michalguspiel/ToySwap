package com.erdees.toyswap.model.models

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

data class Item(
    val name: String = "",
    val category: DocumentReference = Firebase.firestore.collection("itemCategories").document(),
    val description: String = "",
    val price: Double = 0.0,
    val mainImageUrl: String = "",
    val otherImagesUrl: List<String> = listOf(),
    val timeStamp : Timestamp = Timestamp.now(),
    val userId: String = "",
    ) {
}