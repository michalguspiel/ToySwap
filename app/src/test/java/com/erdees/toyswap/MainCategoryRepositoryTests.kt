package com.erdees.toyswap

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.erdees.toyswap.model.models.item.ItemCategoriesHandler
import com.erdees.toyswap.viewModel.ChooseCategoryDialogViewModel
import junit.framework.Assert.assertEquals
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class MainCategoryRepositoryTests {

    @Rule @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: ChooseCategoryDialogViewModel

    @Before
    fun setup() {
        viewModel = ChooseCategoryDialogViewModel()
        viewModel.setCategory(null)
    }

    @After
    fun teardown(){

    }

    @Test
    fun `Given repository was just created viewModel get category function should return null`(){
        assertEquals(null,viewModel.categoryLiveData.value)
    }

    @Test
    fun `Given I just changed categoryLivedata value for dolls viewmodel getCategoryLiveData should return dolls`(){
        viewModel.setCategory(ItemCategoriesHandler.Dolls)
        assertEquals(ItemCategoriesHandler.Dolls,viewModel.categoryLiveData.value)
    }


}