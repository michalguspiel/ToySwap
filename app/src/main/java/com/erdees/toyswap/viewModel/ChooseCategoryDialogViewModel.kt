package com.erdees.toyswap.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.erdees.toyswap.model.localDatabase.ItemCategoryRepository
import com.erdees.toyswap.model.localDatabase.LocalDatabase
import com.erdees.toyswap.model.models.item.itemCategory.ItemCategory
import com.erdees.toyswap.model.repositories.CategoryRepository

class ChooseCategoryDialogViewModel(application: Application) : AndroidViewModel(application) {

    private var itemCategoryRepository : ItemCategoryRepository
    private var categoryRepository : CategoryRepository

    init {
        val itemCategoryDao = LocalDatabase.getDatabase(application).itemCategoryDao()

        itemCategoryRepository = ItemCategoryRepository(itemCategoryDao)
        categoryRepository = CategoryRepository.getInstance(itemCategoryRepository)

    }

    fun getItemCategory(id : Int) = itemCategoryRepository.getItemCategory(id)


    fun restartItemCategoriesHandler() = categoryRepository.restartItemCategoriesHandler()

    val categoryLiveData = categoryRepository.getCategoryLiveData()

    val categoriesHandlerLiveData = categoryRepository.getCategoriesHandlerLiveData()

    fun pickCategory(category: ItemCategory?) = categoryRepository.pickCategory(category)

    fun pickPreviousCategory() = categoryRepository.pickPreviousCategory()


}