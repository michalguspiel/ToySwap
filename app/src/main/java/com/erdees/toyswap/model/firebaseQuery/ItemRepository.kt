package com.erdees.toyswap.model.firebaseQuery

import androidx.lifecycle.LifecycleOwner
import com.erdees.toyswap.model.models.Item
import com.firebase.ui.firestore.paging.FirestorePagingOptions

class ItemRepository(private val dao: ItemDao) {

    fun getOptions(viewLifecycleOwner: LifecycleOwner) : FirestorePagingOptions<Item>{
      return dao.getOptions(viewLifecycleOwner)
    }

}