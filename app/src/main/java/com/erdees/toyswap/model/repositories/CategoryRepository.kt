package com.erdees.toyswap.model.repositories

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.erdees.toyswap.model.Constants.STARTING_CATEGORY_ID
import com.erdees.toyswap.model.localDatabase.ItemCategoryRepository
import com.erdees.toyswap.model.localDatabase.LocalDatabase
import com.erdees.toyswap.model.models.item.itemCategory.ItemCategory
import com.erdees.toyswap.model.utils.ItemCategoriesHandler

class CategoryRepository(val context : Context) {



    private var mutableCategory : ItemCategory? = null
    private val chosenCategoryLiveData : MutableLiveData<ItemCategory?> = MutableLiveData<ItemCategory?>()

    private var itemCategoriesHandler = ItemCategoriesHandler()

    private val itemCategoriesHandlerLiveData = MutableLiveData<ItemCategoriesHandler>()

    private  var itemCategoryRepository : ItemCategoryRepository



    init {
        chosenCategoryLiveData.value = mutableCategory
        itemCategoriesHandlerLiveData.value = itemCategoriesHandler
        val itemCategoryDao = LocalDatabase.getDatabase(context).itemCategoryDao()
        itemCategoryRepository = ItemCategoryRepository(itemCategoryDao)
        itemCategoriesHandler.currentCategory = itemCategoryRepository.getItemCategory(STARTING_CATEGORY_ID)
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
        fun getInstance(context: Context) = instance ?:  synchronized(this) {
            instance
                ?: CategoryRepository(context).also {  instance = it}
        }
    }


}