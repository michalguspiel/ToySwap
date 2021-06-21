package com.erdees.toyswap.model.models.item

import android.graphics.drawable.Icon
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

open class ItemCategory (
    val categoryName : String = "",
    val parent : ItemCategory? = null,
    val children : List<ItemCategory>? = null,
    val icon: Icon? = null,
    val docName : String = ""
) {

    fun documentRef() : DocumentReference = Firebase.firestore.document("itemCategories/${this.docName}")
}


