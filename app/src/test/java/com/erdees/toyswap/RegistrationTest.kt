package com.erdees.toyswap

import com.erdees.toyswap.activities.welcomeActivity.Registration
import junit.framework.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner


@RunWith(
    RobolectricTestRunner::class)
class RegistrationTest {

    @Test
    fun testTest(){
        assertEquals(2+2,4)
    }

    @Test
    fun `when mail is not valid registration should not be legitimate and therefore error message should be about mail`(){
    val registration = Registration("testpassword123","testpassword123","notvalidMail@gmailcom")
        val anotherRegistration = Registration("testpassword123","testpassword123","notvalidMailgmail.com")
        assertEquals(registration.isLegit(),false)
        assertEquals(registration.errorMessage,"Provide valid email!")
        assertEquals(anotherRegistration.isLegit(),false)
        assertEquals(anotherRegistration.errorMessage,"Provide valid email!")
    }
    @Test
    fun `when mail is valid format but passwords are not same registration should not be legitimate and error should be about passwords being different`(){
        val registration = Registration("testpassword123","testpasd123","validMail@gmail.com")
        assertEquals(registration.isLegit(),false)
        assertEquals(registration.errorMessage,"Passwords are not same!")
    }
    @Test
    fun `when mail is valid, passwords same, but not long enough error should be about that`(){
        val registration = Registration("tes1","tes1","validMail@gmail.com")
        assertEquals(registration.isLegit(),false)
        assertEquals(registration.errorMessage,"Password too short!")

    }
    @Test
    fun `when everything is valid but passsword doesnt contain digit error should be about that`(){
        val registration = Registration("testtest","testtest","validMail@gmail.com")
        assertEquals(registration.isLegit(),false)
        assertEquals(registration.errorMessage,"Password has to contain at least one digit!")
    }
    @Test
    fun `when everything is ok but password is only digits error should be about that`(){
        val registration = Registration("123456789","123456789","validMail@gmail.com")
        assertEquals(registration.isLegit(),false)
        assertEquals(registration.errorMessage,"Password can't contain digits only!")

    }
    @Test
    fun `when everything is valid registartion should be legit`(){
        val registration = Registration("test123456789","test123456789","validMail@gmail.com")
        assertEquals(registration.isLegit(),true)
        assertEquals(registration.errorMessage,"No error")

    }



}