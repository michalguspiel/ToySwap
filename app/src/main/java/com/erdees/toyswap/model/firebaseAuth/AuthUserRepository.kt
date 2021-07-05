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

class AuthUserRepository(private val authUserDao: AuthUserDao) {

    fun getUserLiveData(): MutableLiveData<FirebaseUser?> {
        return authUserDao.getUserLiveData()
    }

    fun getClientUserLiveData() : MutableLiveData<ToySwapUser?> {
        return authUserDao.getClientUserLiveData()
    }

    fun getIsUserLoggedOutLiveData(): MutableLiveData<Boolean> {
        return authUserDao.getIsUserLoggedOutLiveData()
    }


    fun getUserSignInProvider(): MutableLiveData<String>{
        return authUserDao.getUserSignInProviderLiveData()
    }

    fun updateAddress(address: Address): Task<Void>?{
       return authUserDao.updateAddress(address)
    }


    fun deleteCurrentAvatar() : Task<Void>{
        return authUserDao.deleteCurrentAvatar()
    }

    fun uploadNewAvatar(imageUri: Uri) : UploadTask {
        return  authUserDao.uploadNewAvatar(imageUri)
    }

    fun getNewAvatarUrl() : Task<Uri> {
        return authUserDao.getNewAvatarUrl()
    }

    fun updateAvatar(uri: String) : Task<Void>?{
        return authUserDao.updateFirebaseUserAvatar(uri)
    }

    fun updateNames(firstName: String , lastName: String) : Task<Void>? {
        return authUserDao.updateNames(firstName,lastName)
    }

    fun registerWithPassword(registration: Registration): Task<AuthResult>{
        return authUserDao.signUpWithEmail(registration)
    }

    fun loginWithPassword(email: String, password: String) : Task<AuthResult>{
       return authUserDao.loginWithPassword(email, password)
    }

    fun firebaseAuthWithGoogle(idToken: String) : Task<AuthResult> {
      return   authUserDao.firebaseAuthWithGoogle(idToken)
    }

    fun signOut(){
        authUserDao.signOut()
    }

    fun reAuthenticate(cred: AuthCredential): Task<Void>?{
       return authUserDao.reAuthenticate(cred)
    }

    fun changePassword(registration: Registration): Task<Void>?{
        return authUserDao.changePassword(registration)
    }



}