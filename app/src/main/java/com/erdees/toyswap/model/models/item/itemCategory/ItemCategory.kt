package com.erdees.toyswap.model.models.item.itemCategory

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.erdees.toyswap.DrawableStaticMap
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

@Entity(tableName = "item_category")
data class ItemCategory(
    @PrimaryKey val id: Int,
    val name: String,
    val iconRef: String?
) {
    @Ignore
    val docRef: DocumentReference = Firebase.firestore.collection("itemCategories").document(name)

    @Ignore
    val icon: Int? = DrawableStaticMap.map[iconRef]



}


@Entity(tableName = "item_category_cross_ref")
data class ItemCategoryCrossRef(
    val parentId: Int,
    val childId: Int
)
