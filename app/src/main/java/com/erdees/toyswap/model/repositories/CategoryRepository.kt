package com.erdees.toyswap.model.repositories

import androidx.lifecycle.MutableLiveData
import com.erdees.toyswap.model.models.item.ItemCategoriesHandler
import com.erdees.toyswap.model.models.item.ItemCategory

class CategoryRepository {

    private var mutableCategory : ItemCategory? = null
    private val chosenCategoryLiveData : MutableLiveData<ItemCategory?> = MutableLiveData<ItemCategory?>()

    private var itemCategoriesHandler = ItemCategoriesHandler()
    private val itemCategoriesHandlerLiveData = MutableLiveData<ItemCategoriesHandler>()



    init {
        chosenCategoryLiveData.value = mutableCategory
        itemCategoriesHandlerLiveData.value = itemCategoriesHandler
    }

    fun pickCategory(category: ItemCategory?) {
        if (category != null) {
            itemCategoriesHandler.pickCategory(category)
            if(itemCategoriesHandler.isCurrentCategoryFinal()) updateChosenCategory(category)
            else itemCategoriesHandlerLiveData.value = itemCategoriesHandler
        }
    }

    fun pickPreviousCategory(){
        itemCategoriesHandler.pickPreviousCategory()
        itemCategoriesHandlerLiveData.value = itemCategoriesHandler
    }

    private fun updateChosenCategory(pickedCategory: ItemCategory?) {
        mutableCategory = pickedCategory
        chosenCategoryLiveData.value = mutableCategory
        clearItemCategoriesHandler()
    }

     fun clearItemCategoriesHandler() {
        itemCategoriesHandler = ItemCategoriesHandler()
        itemCategoriesHandlerLiveData.value = itemCategoriesHandler
    }

    fun clearCategory() = updateChosenCategory(null)

    fun getCategoryLiveData() = chosenCategoryLiveData

    fun getCategoriesLiveData() = itemCategoriesHandlerLiveData

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