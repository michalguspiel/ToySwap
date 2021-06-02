package com.erdees.toyswap.model

/**
 * I decided to make this sort of "NODES" in order to implement feature where user have to
 * pick one of the categories that his item will be placed in. I don't really know if this should be in the server
 * or hard coded like this. Everything is in one place and I don't really see why this would have to be changed really often.
 * */

class ItemCategories {
    /**Main Categories*/
    object Kids : ItemCategory("Kids", null, listOf(Toys))
    object Sports : ItemCategory("Sport",null, listOf(Bikes,Teamsports))
    /**Sub Categories*/
    object Bikes : ItemCategory("Bikes", Sports, listOf(AdultBikes, KidsBikes))
    object Teamsports : ItemCategory("Team sports", Sports, null)
    object Individualsports : ItemCategory("Individual sports", Sports, null)
    object Toys : ItemCategory("Toys", Kids, listOf(Cars, Dolls))

    /**Children categories*/
    object Dolls : ItemCategory("Toys", Toys, null)
    object Cars : ItemCategory("Toys", Toys, null)

    object AdultBikes : ItemCategory("Adult bikes",Bikes,null)
    object KidsBikes : ItemCategory("Kids bikes",Bikes,null)

}