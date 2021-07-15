package com.erdees.toyswap.viewModel

import androidx.lifecycle.ViewModel
import com.erdees.toyswap.model.models.item.ItemCategory
import com.erdees.toyswap.model.repositories.CategoryRepository

class ChooseCategoryDialogViewModel : ViewModel() {


    private val categoryRepository = CategoryRepository.getInstance()

    fun clearItemCategoriesHandler() = categoryRepository.clearItemCategoriesHandler()

    val categoryLiveData = categoryRepository.getCategoryLiveData()

    val categoriesLiveData = categoryRepository.getCategoriesLiveData()

    fun pickCategory(category: ItemCategory?) = categoryRepository.pickCategory(category)

    fun pickPreviousCategory() = categoryRepository.pickPreviousCategory()


}