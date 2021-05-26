package com.erdees.toyswap.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.erdees.toyswap.model.repositories.AuthRepository

class MyAccountFragmentViewModel(application: Application) : AndroidViewModel(application) {

    private val authRepository = AuthRepository(application)

    val userLiveData = authRepository.getUserLiveData()

    val isUserLoggedOutLiveData = authRepository.getIsUserLoggedOutLiveData()

    fun signOut() {
        authRepository.signOut()
    }

}