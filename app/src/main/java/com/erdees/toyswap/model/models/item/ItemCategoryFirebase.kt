package com.erdees.toyswap.model.models.item

import com.google.firebase.firestore.DocumentReference

data class ItemCategoryFirebase(
    val categoryName: String = "",
    val parentRef: DocumentReference? = null,
    val childrenRef: List<DocumentReference>? = null,
    val docRef: DocumentReference? = null
)