package com.erdees.toyswap.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.erdees.toyswap.model.repositories.AuthRepository

class MainActivityViewModel(application: Application): AndroidViewModel(application) {

    private val authRepository : AuthRepository = AuthRepository.getInstance(application)
    val userLiveData = authRepository.getUserLiveData()
    val isUserLoggedIn = authRepository.getIsUserLoggedOutLiveData()
}