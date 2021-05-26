package com.erdees.toyswap.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.erdees.toyswap.model.Registration
import com.erdees.toyswap.model.repositories.AuthRepository

class LoginActivityViewModel(application: Application): AndroidViewModel(application) {

    private val authRepository : AuthRepository = AuthRepository(application)

    val userLiveData = authRepository.getUserLiveData()

    val isUserLoggedInLiveData = authRepository.getIsUserLoggedOutLiveData()

    fun registerWithEmail(registration: Registration) {
        authRepository.registerWithPassword(registration)
    }

    fun loginWithEmail(email: String, password: String){
        authRepository.loginWithPassword(email,password)
    }

    fun signUpWithGoogle(token : String) {
        authRepository.firebaseAuthWithGoogle(token)
    }

    fun updateUserLiveData() {
        authRepository.updateUserLiveData()
    }
}