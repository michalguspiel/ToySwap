package com.erdees.toyswap.model.firebaseQuery

import androidx.lifecycle.LifecycleOwner
import com.erdees.toyswap.model.models.item.Item
import com.firebase.ui.firestore.paging.FirestorePagingOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference

class ItemRepository(private val dao: ItemDao) {

    fun getOptions(viewLifecycleOwner: LifecycleOwner) : FirestorePagingOptions<Item>{
      return dao.getOptions(viewLifecycleOwner)
    }


    fun addItemToFirebase(item: Item):Task<DocumentReference> = dao.addItemToFirebase(item)



    fun getPresentedItem() = dao.getPresentedItem()

    fun setItemToPresent(itemToPresent: Item) = dao.setItemToPresent(itemToPresent)

}