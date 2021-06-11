package com.erdees.toyswap

import com.erdees.toyswap.model.CustomListRearranger.rearrange
import junit.framework.Assert.assertEquals
import org.junit.Test

class CustomListRearrangerTest {


    @Test
    fun `Given i have a list of numbers from 0-5 and i want to last element to first position list should be 0,5,1,2,3,4`(){
        val testList = listOf(0,1,2,3,4,5)
        assertEquals(listOf(0,5,1,2,3,4),testList.rearrange(5,1))
    }

    @Test
    fun `Given i have list 0,1,2,3 and i want first element to be on 2 pos return list should be 1,2,0,3`(){
        val testList = listOf(0,1,2,3)
        assertEquals(listOf(1,2,0,3),testList.rearrange(0,2))
    }

    @Test
    fun `Given i have list 0 to 9 and i want first element to be in the fifth pos, return list should be 1,2,3,4,5,0,6,7,8,9`(){
        val testList = listOf(0,1,2,3,4,5,6,7,8,9)
        assertEquals(listOf(1,2,3,4,5,0,6,7,8,9),testList.rearrange(0,5))
    }

    @Test
    fun `Given i have list 0 to 9 and i want last element to be in the fifth pos, return list should be 0,1,2,3,4,9,5,0,6,7,8`(){
        val testList = listOf(0,1,2,3,4,5,6,7,8,9)
        assertEquals(listOf(0,1,2,3,4,9,5,6,7,8),testList.rearrange(9,5))
    }

    @Test
    fun `Given i have list 1 to 20 and im changing last number to 10 pos and 15th element to 17th pos return list should be correct `(){
        val range = 1 until 21
        val list = mutableListOf<Int>()
        for (i in range)list += i
        list.forEach { print("$it ") }
        val newList = list.rearrange(19,10)
        val finalList = newList.rearrange(15,17)
        assertEquals(listOf(1,2,3,4,5,6,7,8,9,10,20,11,12,13,14,16,17,15,18,19),finalList)
    }

    @Test
    fun `TESTING LONG LIST`(){
        val range = 0 until(9999)
        var mutableList = mutableListOf<Int>()
        for(i in range) mutableList.add(i)
        val changedList = mutableList.rearrange(0,9998)

        var checkList = mutableListOf<Int>()
        for(i in range.drop(1)) checkList.add(i)
        checkList.add(range.first)
        assertEquals(changedList,checkList)



    }

}