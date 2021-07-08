package com.erdees.toyswap.model.models.item

import android.graphics.drawable.Icon
import android.util.Log
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


open class ItemCategory (
    val categoryName : String,
    val parent : ItemCategory?,
    val children : List<ItemCategory>?,
    val icon: Icon? = null,
    val docRef : DocumentReference = Firebase.firestore.collection("itemCategories").document(categoryName)
) {

   fun toFirebaseItemCategory() : ItemCategoryFirebase {
       if (parent != null) {
           Log.i("ITEMCATEGORY" , "Parent! = ${parent.categoryName}")
       }
       return ItemCategoryFirebase(categoryName,parent?.docRef,children?.map { it.docRef },docRef)
   }

    override fun toString(): String {
        return "$categoryName  ${parent?.categoryName}  ${children?.joinToString { it.categoryName }}  $docRef"
    }

}




