package com.erdees.toyswap

import com.erdees.toyswap.model.utils.CustomListRearranger.rearrange
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class CustomListRearrangerTest {


    lateinit var testableList : List<Int>

    @Before
    fun setUp(){
        testableList = listOf(0,1,2)
    }

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
        val mutableList = mutableListOf<Int>()
        for(i in range) mutableList.add(i)
        val changedList = mutableList.rearrange(0,9998)

        val checkList = mutableListOf<Int>()
        for(i in range.drop(1)) checkList.add(i)
        checkList.add(range.first)
        assertEquals(changedList,checkList)
    }

    @Test
    fun `Given I want to place second element in last place, second element should have last spot in the list`(){
        val testList = listOf(0,1,2,3,4,5,6,7,8,9)
        val newList = testList.rearrange(1,9)
        assertEquals(listOf(0,2,3,4,5,6,7,8,9,1),newList)

        val anotherTest = testList.rearrange(1,8)
        assertEquals(listOf(0,2,3,4,5,6,7,8,1,9),anotherTest)
    }


    @Test
    fun `given I want to place first item just before last one list result should be 1,0,2`(){
        val result = testableList.rearrange(0,1)
        assertEquals(listOf(1,0,2), result)
    }

    @Test
    fun `given I want to place first item as last one list result should be 1,2,0`(){
        val result = testableList.rearrange(0,2)
        assertEquals(listOf(1,2,0), result)
    }


    @Test
    fun `given I want to place last item in the middle result should be 0,2,1`(){
        val result = testableList.rearrange(2,1)
        assertEquals(listOf(0,2,1), result)
    }

    @Test
    fun `given I want to place last item as first result should be 2,0,1`(){
        val result = testableList.rearrange(2,0)
        assertEquals(listOf(2,0,1), result)
    }


    @Test
    fun `given by accident 2 pos wants to be switched with 2 pos all should stay same`(){
        val result = testableList.rearrange(2,2)
        assertEquals(testableList,result)
    }

    @Test
    fun `given I want to place second item in first place result should be 1,0,2`(){
        val result = testableList.rearrange(1,0)
    assertEquals(listOf(1,0,2),result)

    }


    @Test
    fun `testing operation and then reverse operation` (){
        var result = testableList.rearrange(0,1)
        assertEquals(listOf(1,0,2), result)
        result = result.rearrange(1,0)
        assertEquals(testableList,result)
    }

    @Test
    fun `testing few operations`(){
        repeat(5){
            testableList = testableList.rearrange(0,1)
                .rearrange(1,0) as List<Int>
        }
        assertEquals(true,testableList.contains(1) && testableList.contains(0) && testableList.contains(2))    }

    @Test
    fun `testing many operations`(){
        repeat(99){
           testableList =  testableList.rearrange(0,1).rearrange(1,0) as List<Int>
            testableList = testableList
                .rearrange(1,2)
                .rearrange(2,1)
                .rearrange(2,0) as List<Int>
           testableList =  testableList.rearrange(1,0)
                .rearrange(2,1)
                .rearrange(0,2) as List<Int>
        }
        println("${testableList[0]} ${testableList[1]} ${testableList[2]}")
        assertEquals(true,testableList.contains(1) && testableList.contains(0) && testableList.contains(2))
    }

}