package com.erdees.toyswap.viewModel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import com.erdees.toyswap.model.firebase.AuthDao
import com.erdees.toyswap.model.firebase.AuthRepository
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.UploadTask

class MyAccountFragmentViewModel(application: Application) : AndroidViewModel(application) {

    private val authRepository : AuthRepository



    init {
        val authDao = AuthDao.getInstance(application)
        authRepository = AuthRepository(authDao)
    }

    val clientUserData = authRepository.getClientUserLiveData()

    val userLiveData = authRepository.getUserLiveData()

    val addressLiveData = authRepository.getUserAddressLiveData()

    val isUserLoggedOutLiveData = authRepository.getIsUserLoggedOutLiveData()

    val userAuthProviderLiveData = authRepository.getUserSignInProvider()


    fun deleteCurrentAvatar() : Task<Void>{
        return authRepository.deleteCurrentAvatar()
    }

    fun uploadNewAvatar(imageUri: Uri) : UploadTask {
        return  authRepository.uploadNewAvatar(imageUri)
    }

    fun getNewAvatarUrl() : Task<Uri> {
        return authRepository.getNewAvatarUrl()
    }

    fun updateAvatar(uri: String): Task<Void>? {
        return authRepository.updateAvatar(uri)
    }

    fun signOut() {
        authRepository.signOut()
    }

}