package com.erdees.toyswap.viewModel

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import com.erdees.toyswap.model.firebaseQuery.ItemDao
import com.erdees.toyswap.model.firebaseQuery.ItemRepository
import com.erdees.toyswap.model.models.item.Item
import com.firebase.ui.firestore.paging.FirestorePagingOptions

class BrowserFragmentViewModel : ViewModel() {

    private var itemRepository : ItemRepository

    init{
        val itemDao = ItemDao.getInstance()
        itemRepository = ItemRepository(itemDao)
    }

    fun getOptions(viewLifecycleOwner: LifecycleOwner): FirestorePagingOptions<Item> {
       return itemRepository.getOptions(viewLifecycleOwner)
    }

}