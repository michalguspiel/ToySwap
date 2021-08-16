package com.erdees.toyswap.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.erdees.toyswap.model.models.item.itemCategory.ItemCategory
import com.erdees.toyswap.model.repositories.CategoryRepository

class ChooseCategoryDialogViewModel(application: Application) : AndroidViewModel(application) {


    private val categoryRepository = CategoryRepository.getInstance(application)

    fun clearItemCategoriesHandler() = categoryRepository.clearItemCategoriesHandler()

    val categoryLiveData = categoryRepository.getCategoryLiveData()

    val categoriesLiveData = categoryRepository.getCategoriesLiveData()

    fun pickCategory(category: ItemCategory?) = categoryRepository.pickCategory(category)

    fun pickPreviousCategory() = categoryRepository.pickPreviousCategory()


}