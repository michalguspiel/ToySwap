package com.erdees.toyswap.viewModel

import androidx.lifecycle.ViewModel
import com.erdees.toyswap.model.firebaseQueries.item.ItemDao
import com.erdees.toyswap.model.firebaseQueries.item.ItemRepository
import com.erdees.toyswap.model.models.item.Item

class BrowserFragmentRvAdapterViewModel: ViewModel() {

    private var itemRepository : ItemRepository

    init{
        val itemDao = ItemDao.getInstance()
        itemRepository = ItemRepository(itemDao)
    }

    fun setItemToPresent(item: Item) = itemRepository.setItemToPresent(item)
}