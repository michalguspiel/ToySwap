package com.erdees.toyswap.model.localDatabase

class ItemCategoryRepository(private val itemCategoryDao : ItemCategoryDao) {

    fun getItemCategory(id: Int) = itemCategoryDao.getItemCategory(id)

    fun getItemChildren(id:Int) = itemCategoryDao.getCategoryChildren(id)

  // fun getItemParent(id:Int) = itemCategoryDao.getCategoryParent(id)


}