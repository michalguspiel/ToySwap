package com.erdees.toyswap.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.erdees.toyswap.model.localDatabase.ItemCategoryRepository
import com.erdees.toyswap.model.localDatabase.LocalDatabase

class ChatFragmentViewModel (application: Application) : AndroidViewModel(application) {

    private var localDatabaseRepository : ItemCategoryRepository

    init {
        val itemCategoryDao = LocalDatabase.getDatabase(application).itemCategoryDao()
        localDatabaseRepository = ItemCategoryRepository(itemCategoryDao)
    }

    fun getItemCategory(id : Int) = localDatabaseRepository.getItemCategory(id)

}