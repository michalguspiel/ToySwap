package com.erdees.toyswap.model.utils

object CustomListRearranger {

    fun List<Any>.rearrange(indexOfElementToMove: Int, finalIndexOfElement: Int) :List<Any>{
        if(indexOfElementToMove == finalIndexOfElement) return this
        val elementToMove = this[indexOfElementToMove]
        var counter = 0
        val newList: MutableList<Any> = mutableListOf()
        for(i in this.indices){
            when(i){
                indexOfElementToMove -> {
                    if(counter == indexOfElementToMove)  counter ++
                    newList.add(this[counter])
                    counter++
                    if(counter == indexOfElementToMove) counter ++
                }
                finalIndexOfElement ->{
                    newList.add(elementToMove)
                }
                else -> {
                    newList.add(this[counter])
                    counter++
                }
            }
        }
        return newList
    }
}