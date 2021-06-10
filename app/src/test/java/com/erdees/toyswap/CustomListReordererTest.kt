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
}