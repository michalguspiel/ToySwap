package com.erdees.toyswap.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.erdees.toyswap.model.repositories.AuthRepository

class MyAccountFragmentViewModel(application: Application) : AndroidViewModel(application) {

    private val authRepository : AuthRepository = AuthRepository.getInstance(application)


    val userLiveData = authRepository.getUserLiveData()

    val addressLiveData = authRepository.getUserAddressLiveData()

    val isUserLoggedOutLiveData = authRepository.getIsUserLoggedOutLiveData()

    fun signOut() {
        authRepository.signOut()
    }

}