package com.erdees.toyswap.viewModel

import androidx.lifecycle.ViewModel
import com.erdees.toyswap.model.firebaseQuery.ItemDao
import com.erdees.toyswap.model.firebaseQuery.ItemRepository

class ItemFragmentViewModel: ViewModel() {

    private var itemRepository : ItemRepository

    init {
        val itemDao = ItemDao.getInstance()
        itemRepository = ItemRepository(itemDao)
    }

    fun getPresentedItem() = itemRepository.getPresentedItem()

}