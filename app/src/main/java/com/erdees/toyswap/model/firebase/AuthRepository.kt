package com.erdees.toyswap.model.firebase

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.erdees.toyswap.model.Address
import com.erdees.toyswap.model.Registration
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.storage.UploadTask

class AuthRepository(private val authDao: AuthDao) {

    fun getUserLiveData(): MutableLiveData<FirebaseUser?> {
        return authDao.getUserLiveData()
    }

    fun getClientUserLiveData() : MutableLiveData<ClientUser?> {
        return authDao.getClientUserLiveData()
    }

    fun getIsUserLoggedOutLiveData(): MutableLiveData<Boolean> {
        return authDao.getIsUserLoggedOutLiveData()
    }

    fun getUserAddressLiveData(): MutableLiveData<Address> {
        return authDao.getUserAddressLiveData()
    }

    fun getUserSignInProvider(): MutableLiveData<String>{
        return authDao.getUserSignInProviderLiveData()
    }

    fun updateAddress(address: Address): Task<Void>?{
       return authDao.updateAddress(address)
    }

    fun deleteCurrentAvatar() : Task<Void>{
        return authDao.deleteCurrentAvatar()
    }

    fun uploadNewAvatar(imageUri: Uri) : UploadTask {
        return  authDao.uploadNewAvatar(imageUri)
    }

    fun getNewAvatarUrl() : Task<Uri> {
        return authDao.getNewAvatarUrl()
    }

    fun updateAvatar(uri: String) : Task<Void>?{
        return authDao.updateFirebaseUserAvatar(uri)
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