package com.erdees.toyswap.model.models.item

/**
 * I decided to make this sort of "NODES" in order to implement feature where user have to
 * pick one of the categories that his item will be placed in. I don't really know if this should be in the server
 * or hard coded like this. Everything is in one place and I don't really see why this would have to be changed really often.
 * */

class ItemCategories {
    object MainCategory : ItemCategory("Category",null,listOf(Kids, Sports),docName = "category")
    /**Main Categories*/
    object Kids : ItemCategory("Kids", MainCategory, listOf(Toys),docName = "kids")
    object Sports : ItemCategory("Sport",MainCategory, listOf(Bikes, Teamsports, Individualsports),docName = "sports")
    /**Sub Categories*/
    object Bikes : ItemCategory("Bikes", Sports, listOf(AdultBikes, KidsBikes),docName = "bikes")
    object Teamsports : ItemCategory("Team sports", Sports, null,docName = "teamSports")
    object Individualsports : ItemCategory("Individual sports", Sports, null,docName = "individualSports")
    object Toys : ItemCategory("Toys", Kids, listOf(Cars, Dolls),docName = "toys")

    /**Children categories*/
    object Dolls : ItemCategory("Dolls", Toys, null,docName = "dolls")
    object Cars : ItemCategory("Cars", Toys, null,docName = "cars")

    object AdultBikes : ItemCategory("Adult bikes", Bikes,listOf(MountainBike),docName = "adultBikes")
    object KidsBikes : ItemCategory("Kids bikes", Bikes,null,docName = "kidsBikes")


    object MountainBike : ItemCategory("Mountain Bikes",Bikes,null,docName = "mountainBikes")


    var currentCategory: ItemCategory = MainCategory


    fun isCategoryFinal(): Boolean {
      return  currentCategory.children == null
    }

    fun pickCategory(pickedCategory: ItemCategory) {
        currentCategory = pickedCategory
    }

}