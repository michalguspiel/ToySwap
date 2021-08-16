package com.erdees.toyswap.model.utils

import com.erdees.toyswap.model.models.item.itemCategory.ItemCategory


class ItemCategoriesHandler {

    lateinit var currentCategory: ItemCategory

    var categoriesStack: List<ItemCategory> = listOf()

    fun isCurrentCategoryFinal(): Boolean {
        // TODO TAKE THIS CATEGORY ID IN SQLITE, GET ALL CROSS REFERENCES, IF DOESNT HAVE CHILDREN RETURN TRUE ELSE FALSE
    }

    fun pickCategory(pickedCategory: ItemCategory) {
        categoriesStack = categoriesStack + currentCategory
        currentCategory = pickedCategory
    }

    fun pickPreviousCategory() {
        currentCategory = categoriesStack.last()
        categoriesStack = categoriesStack - categoriesStack.last()
    }