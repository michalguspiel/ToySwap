package com.erdees.toyswap.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.erdees.toyswap.model.Registration
import com.erdees.toyswap.model.firebaseAuth.AuthUserDao
import com.erdees.toyswap.model.firebaseAuth.AuthUserRepository
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult

class LoginActivityViewModel(application: Application): AndroidViewModel(application) {

    private val authUserRepository : AuthUserRepository

    init {
        val authDao = AuthUserDao.getInstance(application)
        authUserRepository = AuthUserRepository(authDao)
    }

    val clientUserLiveData = authUserRepository.getClientUserLiveData()


    fun registerWithEmail(registration: Registration) : Task<AuthResult> {
        return authUserRepository.registerWithPassword(registration)
    }

    fun loginWithEmail(email: String, password: String): Task<AuthResult>{
        return authUserRepository.loginWithPassword(email,password)
    }

    fun signUpWithGoogle(token : String):Task<AuthResult> {
        return authUserRepository.firebaseAuthWithGoogle(token)
    }

}