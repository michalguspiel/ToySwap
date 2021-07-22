package com.erdees.toyswap.model.models.item

/**
 * I decided to make this sort of "NODES" in order to implement feature where user have to
 * pick one of the categories that his item will be placed in. I don't really know if this should be in the server
 * or hard coded like this. Everything is in one place and I don't really see why this would have to be changed really often.
 * */

class ItemCategoriesHandler {

    object MainCategory : ItemCategory("MainCategory",listOf(Kids, Sports))
    /**Main Categories*/
    object Kids : ItemCategory("Kids", listOf(Toys))
    object Sports : ItemCategory("Sport", listOf(Bikes, Teamsports, Individualsports))

    /**Sub Categories*/
    object Bikes : ItemCategory("Bikes", listOf(AdultBikes, KidsBikes))
    object Teamsports : ItemCategory("Team sports", null)
    object Individualsports : ItemCategory("Individual sports", null)
    object Toys : ItemCategory("Toys", listOf(Cars, Dolls))

    /**Children categories*/
    object Dolls : ItemCategory("Dolls", null)
    object Cars : ItemCategory("Cars", null)

    object AdultBikes : ItemCategory("Adult bikes",listOf(MountainBike))
    object KidsBikes : ItemCategory("Kids bikes",null)


    object MountainBike : ItemCategory("Mountain Bikes",null)


    private val listOfAllCategories = listOf(MainCategory,Kids,Sports,Bikes,Teamsports,Individualsports,Toys,Dolls,Cars,AdultBikes,KidsBikes,MountainBike)

    var currentCategory: ItemCategory = MainCategory

    var categoriesStack : List<ItemCategory> = listOf()

    fun isCurrentCategoryFinal(): Boolean {
        return  currentCategory.children == null
    }

    fun pickCategory(pickedCategory: ItemCategory) {
        categoriesStack = categoriesStack + currentCategory
        currentCategory = pickedCategory
    }

    fun pickPreviousCategory(){
        currentCategory = categoriesStack.last()
        categoriesStack = categoriesStack - categoriesStack.last()
    }
/**
    fun findCategoryFromDocumentReference(docRef : DocumentReference): ItemCategory {
       return listOfAllCategories.first { it.docRef == docRef }
    }

    fun pickCategoryFromDocumentReference(docRef : DocumentReference) {
       currentCategory = listOfAllCategories.first { it.docRef == docRef }
    }*/
}