package com.erdees.toyswap.model.utils

import com.erdees.toyswap.model.models.item.itemCategory.ItemCategory
import com.erdees.toyswap.model.repositories.CategoryRepository


class ItemCategoriesHandler(private val categoryRepository: CategoryRepository) {

    /**currentCategory is initialized in [CategoryRepository] just before injecting it to this [ItemCategoriesHandler]*/
    lateinit var currentCategory: ItemCategory

    private var categoriesStack: List<ItemCategory> = listOf()

    fun currentCategoryChildren(): List<ItemCategory> {
        val childrenIdList = categoryRepository.itemCategoryRepository.getItemChildren(currentCategory.id)
        var childrenList: List<ItemCategory> = listOf()
        for (eachChild in childrenIdList.itemCategoryChildren) {
            childrenList = childrenList + categoryRepository.itemCategoryRepository.getItemCategory(eachChild.id)
        }
        return childrenList
    }

    fun isCurrentCategoryFinal(): Boolean {
       return categoryRepository.itemCategoryRepository.getItemChildren(currentCategory.id).itemCategoryChildren.isEmpty()
    }

    fun pickCategory(pickedCategory: ItemCategory) {
        categoriesStack = categoriesStack + currentCategory
        currentCategory = pickedCategory
    }

    fun pickPreviousCategory() {
        currentCategory = categoriesStack.last()
        categoriesStack = categoriesStack - categoriesStack.last()
    }

}