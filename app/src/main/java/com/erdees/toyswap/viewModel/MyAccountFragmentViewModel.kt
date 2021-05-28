package com.erdees.toyswap.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.erdees.toyswap.model.firebase.AuthDao
import com.erdees.toyswap.model.firebase.AuthRepository
import com.google.android.gms.tasks.Task

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

    fun updateAvatar(uri: String): Task<Void>? {
        return authRepository.updateAvatar(uri)
    }

    fun signOut() {
        authRepository.signOut()
    }

}