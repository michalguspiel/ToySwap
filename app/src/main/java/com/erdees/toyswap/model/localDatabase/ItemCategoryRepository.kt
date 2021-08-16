package com.erdees.toyswap.model.localDatabase

class ItemCategoryRepository(val dao : ItemCategoryDao) {

    fun getItemCategory(id: Int) = dao.getItemCategory(id)
}