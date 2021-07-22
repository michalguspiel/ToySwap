package com.erdees.toyswap

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.erdees.toyswap.model.models.item.ItemCategoriesHandler
import com.google.firebase.FirebaseApp
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class CategoriesInstrumentedTest {

    private lateinit var category: ItemCategoriesHandler
    val appContext = InstrumentationRegistry.getInstrumentation().targetContext


    @Before
    fun setup(){
        FirebaseApp.initializeApp(appContext)
        category = ItemCategoriesHandler()
    }

    @Test
    fun givenIPickedForTheFirstTimeCategory_currentCategoryShouldBeTheOneIPicked(){
        assert(category.currentCategory == ItemCategoriesHandler.MainCategory)
        category.pickCategory(ItemCategoriesHandler.Sports)
        assert(category.currentCategory == ItemCategoriesHandler.Sports)
    }
    @Test
    fun givenIJustInitilizedItemCategories_CurrentCategoryShouldBeStartingCategory(){
        assert(category.currentCategory == ItemCategoriesHandler.MainCategory)
    }
    @Test
    fun givenCategoryKidsGotPicked_CurrentCategoryShouldNotBeSports(){
        assert(category.currentCategory != ItemCategoriesHandler.Sports)
        category.pickCategory(ItemCategoriesHandler.Kids)
        assert(category.currentCategory != ItemCategoriesHandler.Sports)
        assert(category.currentCategory == ItemCategoriesHandler.Kids)
    }

    @Test
    fun givenIJustStartedToPickCategories_CurrentCategoryShouldNotBeFinalButWhenIReachFinalCategory_isCategoryFinalShouldEqualTrue(){
        assert(!category.isCurrentCategoryFinal())
        category.pickCategory(ItemCategoriesHandler.Sports)
        assert(!category.isCurrentCategoryFinal())
        category.pickCategory(ItemCategoriesHandler.Bikes)
        assert(!category.isCurrentCategoryFinal())
        category.pickCategory(ItemCategoriesHandler.AdultBikes)
        assert(category.currentCategory == ItemCategoriesHandler.AdultBikes)
        category.pickCategory(ItemCategoriesHandler.MountainBike)
        assert(category.isCurrentCategoryFinal())

    }

    @Test
    fun givenIJustPickedSportsCategoryAndThenPickedPreviousCategory_CurrentCategoryShouldBeMainCategory(){
        category.pickCategory(ItemCategoriesHandler.Sports)
        assertEquals(category.currentCategory,ItemCategoriesHandler.Sports)
        category.pickPreviousCategory()
        assertEquals(ItemCategoriesHandler.MainCategory,category.currentCategory)
    }

    @Test
    fun givenIJustPickedSportsCategory_CurrentCategoryChildrenShouldBeBikesTeamsportsIndividualsports(){
        category.pickCategory(ItemCategoriesHandler.Sports)
        assertEquals(category.currentCategory,ItemCategoriesHandler.Sports)
        assertEquals(listOf(ItemCategoriesHandler.Bikes,ItemCategoriesHandler.Teamsports,ItemCategoriesHandler.Individualsports),category.currentCategory.children)
    }

    @Test
    fun givenIJustPickedToys_CurrentCategoryChildrenShouldBeDollsAndCars(){
        category.pickCategory(ItemCategoriesHandler.Toys)
        assertEquals(category.currentCategory.children,listOf(ItemCategoriesHandler.Cars,
            ItemCategoriesHandler.Dolls
        ))
    }

    @Test
    fun givenImGoingToBePickingCategoriesUntillIReachCarsAndThenGoingBack_EverthingShouldWork(){
        category.pickCategory(ItemCategoriesHandler.Kids)
        category.pickCategory(ItemCategoriesHandler.Dolls)
        assertEquals(category.currentCategory,ItemCategoriesHandler.Dolls)
        category.pickPreviousCategory()
        assertEquals(category.currentCategory, ItemCategoriesHandler.Kids)
        category.pickPreviousCategory()
        assertEquals(category.currentCategory,ItemCategoriesHandler.MainCategory)
    }

}