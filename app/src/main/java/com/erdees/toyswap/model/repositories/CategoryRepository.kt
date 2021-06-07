package com.erdees.toyswap.model.repositories

import androidx.lifecycle.MutableLiveData
import com.erdees.toyswap.model.models.item.ItemCategory

class CategoryRepository {

    private var mutableCategory : ItemCategory? = null
    private val chosenCategoryLiveData : MutableLiveData<ItemCategory?> = MutableLiveData<ItemCategory?>()

    init {
        chosenCategoryLiveData.value = mutableCategory
    }

    fun updateChosenCategory(pickedCategory: ItemCategory?) {
        mutableCategory = pickedCategory
        chosenCategoryLiveData.value = mutableCategory
    }

    fun getCategoryLiveData() = chosenCategoryLiveData

    /**Singleton*/
    companion object {
        @Volatile
        private var instance: CategoryRepository? = null
        fun getInstance() = instance ?:  synchronized(this) {
            instance
                ?: CategoryRepository().also {  instance = it}
        }
    }


}