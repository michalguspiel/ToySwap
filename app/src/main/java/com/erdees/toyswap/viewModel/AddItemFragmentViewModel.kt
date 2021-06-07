package com.erdees.toyswap.viewModel

import androidx.lifecycle.ViewModel
import com.erdees.toyswap.model.repositories.CategoryRepository

class AddItemFragmentViewModel: ViewModel() {

    private val categoryRepository = CategoryRepository.getInstance()

    val categoryLiveData = categoryRepository.getCategoryLiveData()



}