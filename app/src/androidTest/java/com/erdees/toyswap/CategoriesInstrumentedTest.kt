package com.erdees.toyswap

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.erdees.toyswap.model.models.item.ItemCategories
import com.google.firebase.FirebaseApp
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class CategoriesInstrumentedTest {

    private lateinit var category: ItemCategories
    val appContext = InstrumentationRegistry.getInstrumentation().targetContext


    @Before
    fun setup(){
        FirebaseApp.initializeApp(appContext)
        category = ItemCategories()
    }

    @Test
    fun givenIPickedForTheFirstTimeCategory_currentCategoryShouldBeTheOneIPicked(){
        assert(category.currentCategory == ItemCategories.MainCategory)
        category.pickCategory(ItemCategories.Sports)
        assert(category.currentCategory == ItemCategories.Sports)
    }
    @Test
    fun givenIJustInitilizedItemCategories_CurrentCategoryShouldBeStartingCategory(){
        assert(category.currentCategory == ItemCategories.MainCategory)
    }
    @Test
    fun givenCategoryKidsGotPicked_CurrentCategoryShouldNotBeSports(){
        assert(category.currentCategory != ItemCategories.Sports)
        category.pickCategory(ItemCategories.Kids)
        assert(category.currentCategory != ItemCategories.Sports)
        assert(category.currentCategory == ItemCategories.Kids)
    }

    @Test
    fun givenIJustStartedToPickCategories_CurrentCategoryShouldNotBeFinalButWhenIReachFinalCategory_isCategoryFinalShouldEqualTrue(){
        assert(!category.isCategoryFinal())
        category.pickCategory(ItemCategories.Sports)
        assert(!category.isCategoryFinal())
        category.pickCategory(ItemCategories.Bikes)
        assert(!category.isCategoryFinal())
        category.pickCategory(ItemCategories.AdultBikes)
        assert(category.currentCategory == ItemCategories.AdultBikes)
        category.pickCategory(ItemCategories.MountainBike)
        assert(category.isCategoryFinal())

    }

    @Test
    fun givenIJustPickedSportsCategory_CurrentCategoryParentShouldBeMainCategory(){
        category.pickCategory(ItemCategories.Sports)
        assertEquals(ItemCategories.MainCategory,category.currentCategory.parent)
    }


}