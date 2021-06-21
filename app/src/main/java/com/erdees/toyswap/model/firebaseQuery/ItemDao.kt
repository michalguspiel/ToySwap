package com.erdees.toyswap.model.firebaseQuery

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.paging.PagedList
import com.erdees.toyswap.model.models.item.Item
import com.firebase.ui.firestore.paging.FirestorePagingOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class ItemDao {

    private val db = Firebase.firestore

    private val query = db.collection("items")
      //  .orderBy("timeStamp", Query.Direction.ASCENDING)
        .limit(10)

    private val config = PagedList.Config.Builder()
        .setEnablePlaceholders(false)
        .setPrefetchDistance(5)
        .setPageSize(8)
        .build()

    fun getOptions(viewLifecycleOwner: LifecycleOwner): FirestorePagingOptions<Item> {
        Log.i("ADAPTER", "GETTING OPTIONS")
        return FirestorePagingOptions.Builder<Item>()
            .setLifecycleOwner(viewLifecycleOwner)
            .setQuery(query, config, Item::class.java)
            .build()
    }

    fun addItemToFirebase(Item: Item): Task<DocumentReference> {
       return db.collection("items").add(Item)
    }


/**SINGLETON PATTERN*/
    companion object{
        @Volatile
        private var instance : ItemDao? = null
    fun getInstance() = instance ?: synchronized(this){
        instance
            ?: ItemDao().also{instance = it}
    }
    }

}