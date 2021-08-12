package com.erdees.toyswap.model.localDatabase

class LocalRepository(private val dao : ItemConditionDao) {

    fun getItemConditions() = dao.getItemConditions()

}