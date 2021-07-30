package com.erdees.toyswap.model.models.item

class ItemCondition(val name: String) {

}

class ItemConditions{

    val brandNew = ItemCondition("Brand new")
    val slightlyUsed = ItemCondition("Slightly used")
    val used = ItemCondition("Well used")
    val damaged = ItemCondition("Damaged")
    val broken = ItemCondition("Broken")

    val listOfItemConditions = listOf(brandNew,slightlyUsed,used,damaged,broken)
}