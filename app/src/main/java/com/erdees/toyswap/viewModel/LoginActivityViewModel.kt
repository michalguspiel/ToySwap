package com.erdees.toyswap.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.erdees.toyswap.model.Registration
import com.erdees.toyswap.model.firebase.AuthDao
import com.erdees.toyswap.model.firebase.AuthRepository
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult

class LoginActivityViewModel(application: Application): AndroidViewModel(application) {

    private val authRepository : AuthRepository

    init {
        val authDao = AuthDao.getInstance(application)
        authRepository = AuthRepository(authDao)
    }

    val clientUserLiveData = authRepository.getClientUserLiveData()


    fun registerWithEmail(registration: Registration) : Task<AuthResult> {
        return authRepository.registerWithPassword(registration)
    }

    fun loginWithEmail(email: String, password: String): Task<AuthResult>{
        return authRepository.loginWithPassword(email,password)
    }

    fun signUpWithGoogle(token : String):Task<AuthResult> {
        return authRepository.firebaseAuthWithGoogle(token)
    }

}