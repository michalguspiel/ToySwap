package com.erdees.toyswap.model.models.item

/**
 * I decided to make this sort of "NODES" in order to implement feature where user have to
 * pick one of the categories that his item will be placed in. I don't really know if this should be in the server
 * or hard coded like this. Everything is in one place and I don't really see why this would have to be changed really often.
 * */

class ItemCategories {
    object MainCategory : ItemCategory("Category",null,listOf(Kids, Sports))
    /**Main Categories*/
    object Kids : ItemCategory("Kids", MainCategory, listOf(Toys))
    object Sports : ItemCategory("Sport",MainCategory, listOf(Bikes, Teamsports, Individualsports))
    /**Sub Categories*/
    object Bikes : ItemCategory("Bikes", Sports, listOf(AdultBikes, KidsBikes))
    object Teamsports : ItemCategory("Team sports", Sports, null)
    object Individualsports : ItemCategory("Individual sports", Sports, null)
    object Toys : ItemCategory("Toys", Kids, listOf(Cars, Dolls))

    /**Children categories*/
    object Dolls : ItemCategory("Dolls", Toys, null)
    object Cars : ItemCategory("Cars", Toys, null)

    object AdultBikes : ItemCategory("Adult bikes", Bikes,listOf(MountainBike))
    object KidsBikes : ItemCategory("Kids bikes", Bikes,null)


    object MountainBike : ItemCategory("Mountain Bikes",Bikes,null)


    var currentCategory: ItemCategory = MainCategory


    fun isCategoryFinal(): Boolean {
      return  currentCategory.children == null
    }

    fun pickCategory(pickedCategory: ItemCategory) {
        currentCategory = pickedCategory
    }

}