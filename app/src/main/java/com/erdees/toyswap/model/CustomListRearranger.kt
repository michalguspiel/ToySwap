package com.erdees.toyswap.model

object CustomListRearranger {

    fun List<Any>.rearrange(indexOfElementToMove: Int, finalIndexOfElement: Int) :List<Any>{
        val elementToMove = this[indexOfElementToMove]
        var counter = 0
        val newList: MutableList<Any> = mutableListOf()
        for(i in this.indices){
            when(i){
                indexOfElementToMove -> {
                    if(counter == indexOfElementToMove) counter ++
                    newList.add(this[counter])
                    counter++
                }
                finalIndexOfElement -> newList.add(elementToMove)
                else -> {
                    newList.add(this[counter])
                    counter++
                }
            }
        }
        return newList
    }
    
}