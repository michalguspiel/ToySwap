package com.erdees.toyswap.viewModel

import androidx.lifecycle.ViewModel
import com.erdees.toyswap.model.models.item.ItemCategory
import com.erdees.toyswap.model.repositories.CategoryRepository

class ChooseCategoryDialogViewModel : ViewModel() {


    private val categoryRepository = CategoryRepository.getInstance()

    fun setCategory(category: ItemCategory?) = categoryRepository.updateChosenCategory(category)

    val categoryLiveData = categoryRepository.getCategoryLiveData()

}