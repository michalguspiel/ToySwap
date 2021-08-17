package com.erdees.toyswap.model.repositories

import androidx.lifecycle.MutableLiveData
import com.erdees.toyswap.model.Constants.STARTING_CATEGORY_ID
import com.erdees.toyswap.model.localDatabase.ItemCategoryRepository
import com.erdees.toyswap.model.models.item.itemCategory.ItemCategory
import com.erdees.toyswap.model.utils.ItemCategoriesHandler

class CategoryRepository( val itemCategoryRepository : ItemCategoryRepository) {



    private var mutableCategory : ItemCategory? = null
    private val chosenCategoryLiveData : MutableLiveData<ItemCategory?> = MutableLiveData<ItemCategory?>()

    private var itemCategoriesHandler = ItemCategoriesHandler(this)

    private val itemCategoriesHandlerLiveData = MutableLiveData<ItemCategoriesHandler>()


    init {
        chosenCategoryLiveData.value = mutableCategory
        itemCategoriesHandlerLiveData.value = itemCategoriesHandler
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
        restartItemCategoriesHandler()
    }

     fun restartItemCategoriesHandler() {
         itemCategoriesHandler = ItemCategoriesHandler(this)
         itemCategoriesHandler.currentCategory = itemCategoryRepository.getItemCategory(STARTING_CATEGORY_ID)
         itemCategoriesHandlerLiveData.value = itemCategoriesHandler
    }

    fun clearCategory() = updateChosenCategory(null)

    fun getCategoryLiveData() = chosenCategoryLiveData

    fun getCategoriesHandlerLiveData() = itemCategoriesHandlerLiveData


    /**Singleton*/
    companion object {

        @Volatile
        private var instance: CategoryRepository? = null

        fun getInstance(itemCategoryRepository: ItemCategoryRepository) = instance ?:  synchronized(this) {
            instance
                ?: CategoryRepository(itemCategoryRepository).also {  instance = it}
       }
    }


}