package com.erdees.toyswap.model.firebase

import androidx.lifecycle.MutableLiveData
import com.erdees.toyswap.model.Address
import com.erdees.toyswap.model.Registration
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseUser

class AuthRepository(private val authDao: AuthDao) {

    fun getUserLiveData(): MutableLiveData<FirebaseUser?> {
        return authDao.getUserLiveData()
    }

    fun getIsUserLoggedOutLiveData(): MutableLiveData<Boolean> {
        return authDao.getIsUserLoggedOutLiveData()
    }

    fun getUserAddressLiveData(): MutableLiveData<Address> {
        return authDao.getUserAddressLiveData()
    }

    fun updateAddress(address: Address): Task<Void>?{
       return authDao.updateAddress(address)
    }


    fun registerWithPassword(registration: Registration){
        authDao.registerWithPassword(registration)
    }

    fun loginWithPassword(email: String, password: String){
        authDao.loginWithPassword(email, password)
    }

    fun firebaseAuthWithGoogle(idToken: String) {
        authDao.firebaseAuthWithGoogle(idToken)
    }

    fun signOut(){
        authDao.signOut()
    }

    fun reAuthenticate(cred: AuthCredential): Task<Void>?{
       return authDao.reAuthenticate(cred)
    }

    fun changePassword(registration: Registration): Task<Void>?{
        return authDao.changePassword(registration)
    }

}