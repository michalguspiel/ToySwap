package com.erdees.toyswap.viewModel

import androidx.lifecycle.ViewModel
import com.erdees.toyswap.model.firebaseQuery.ItemDao
import com.erdees.toyswap.model.firebaseQuery.ItemRepository
import com.erdees.toyswap.model.firebaseQuery.SellerDao
import com.erdees.toyswap.model.firebaseQuery.SellerRepository
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot

class ItemFragmentViewModel: ViewModel() {

    private var itemRepository : ItemRepository
    private var sellerRepository : SellerRepository


    init {
        val sellerDao = SellerDao.getInstance()
        val itemDao = ItemDao.getInstance()
        itemRepository = ItemRepository(itemDao)
        sellerRepository = SellerRepository(sellerDao)
    }

    fun getPresentedItem() = itemRepository.getPresentedItem()

    fun getSellerData(sellerId: String) = sellerRepository.getSellerData(sellerId)

    fun downloadSellerFromServer(sellerId: String) : Task<DocumentSnapshot> = sellerRepository.downloadSellerFromServer(sellerId)

}