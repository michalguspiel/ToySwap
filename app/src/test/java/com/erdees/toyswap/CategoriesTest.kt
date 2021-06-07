package com.erdees.toyswap

import com.erdees.toyswap.model.models.item.ItemCategories
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class CategoriesTest {

    lateinit var category: ItemCategories

    @Before
    fun setup(){
        category = ItemCategories()
    }

    @Test
    fun `given i picked for the first time category, current category should be the one i picked`(){
        assert(category.currentCategory == ItemCategories.MainCategory)
        category.pickCategory(ItemCategories.Sports)
        assert(category.currentCategory == ItemCategories.Sports)
    }
    @Test
    fun `given I just initialized itemcategories class, current category should be startingCategory`(){
        assert(category.currentCategory == ItemCategories.MainCategory)
    }
    @Test
    fun `given Im picking category kids, current category shouldnt be sports`(){
        assert(category.currentCategory != ItemCategories.Sports)
        category.pickCategory(ItemCategories.Kids)
        assert(category.currentCategory != ItemCategories.Sports)
        assert(category.currentCategory == ItemCategories.Kids)
    }

    @Test
    fun `given i just start to pick category, isCategoryFinal should be false, but when i reach last category it should be true`(){
        assertEquals(category.isCategoryFinal(), false)
        category.pickCategory(ItemCategories.Sports)
        assertEquals(category.isCategoryFinal(), false)
        category.pickCategory(ItemCategories.Bikes)
        assertEquals(category.isCategoryFinal(), false)
        category.pickCategory(ItemCategories.AdultBikes)
        assert(category.currentCategory == ItemCategories.AdultBikes)
        assertEquals(true, category.isCategoryFinal())


    }

}