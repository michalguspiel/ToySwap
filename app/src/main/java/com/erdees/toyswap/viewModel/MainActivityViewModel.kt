package com.erdees.toyswap.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.erdees.toyswap.model.firebase.AuthDao
import com.erdees.toyswap.model.firebase.AuthRepository

class MainActivityViewModel(application: Application): AndroidViewModel(application) {

    private val authRepository : AuthRepository

    init {
        val authDao = AuthDao.getInstance(application)
        authRepository = AuthRepository(authDao)
    }


    val userLiveData = authRepository.getUserLiveData()
    val isUserLoggedOut = authRepository.getIsUserLoggedOutLiveData()
}