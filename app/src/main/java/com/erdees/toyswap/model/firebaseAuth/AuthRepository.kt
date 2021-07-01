package com.erdees.toyswap.model.firebaseAuth

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.erdees.toyswap.model.Registration
import com.erdees.toyswap.model.models.Address
import com.erdees.toyswap.model.models.user.ToySwapUser
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.storage.UploadTask

class AuthRepository(private val authDao: AuthDao) {

    fun getUserLiveData(): MutableLiveData<FirebaseUser?> {
        return authDao.getUserLiveData()
    }

    fun getClientUserLiveData() : MutableLiveData<ToySwapUser?> {
        return authDao.getClientUserLiveData()
    }

    fun getIsUserLoggedOutLiveData(): MutableLiveData<Boolean> {
        return authDao.getIsUserLoggedOutLiveData()
    }


    fun getUserSignInProvider(): MutableLiveData<String>{
        return authDao.getUserSignInProviderLiveData()
    }

    fun updateAddress(address: Address): Task<Void>?{
       return authDao.updateAddress(address)
    }

    fun updatePublicInfoAboutAddress(city : String) : Task<Void>?{
        return authDao.updatePublicInfoAboutAddress(city)
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

    fun updateNames(firstName: String , lastName: String) : Task<Void>? {
        return authDao.updateNames(firstName,lastName)
    }

    fun registerWithPassword(registration: Registration): Task<AuthResult>{
        return authDao.signUpWithEmail(registration)
    }

    fun loginWithPassword(email: String, password: String) : Task<AuthResult>{
       return authDao.loginWithPassword(email, password)
    }

    fun firebaseAuthWithGoogle(idToken: String) : Task<AuthResult> {
      return   authDao.firebaseAuthWithGoogle(idToken)
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