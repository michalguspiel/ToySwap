package com.erdees.toyswap.viewModel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import com.erdees.toyswap.model.firebaseAuth.AuthUserDao
import com.erdees.toyswap.model.firebaseAuth.AuthUserRepository
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.UploadTask

class MyAccountFragmentViewModel(application: Application) : AndroidViewModel(application) {

    private val authUserRepository : AuthUserRepository



    init {
        val authDao = AuthUserDao.getInstance(application)
        authUserRepository = AuthUserRepository(authDao)
    }

    val clientUserData = authUserRepository.getClientUserLiveData()

    val userLiveData = authUserRepository.getUserLiveData()

    val isUserLoggedOutLiveData = authUserRepository.getIsUserLoggedOutLiveData()

    val userAuthProviderLiveData = authUserRepository.getUserSignInProvider()


    fun deleteCurrentAvatar() : Task<Void>{
        return authUserRepository.deleteCurrentAvatar()
    }

    fun uploadNewAvatar(imageUri: Uri) : UploadTask {
        return  authUserRepository.uploadNewAvatar(imageUri)
    }

    fun getNewAvatarUrl() : Task<Uri> {
        return authUserRepository.getNewAvatarUrl()
    }

    fun updateAvatar(uri: String): Task<Void>? {
        return authUserRepository.updateAvatar(uri)
    }

    fun signOut() {
        authUserRepository.signOut()
    }

}