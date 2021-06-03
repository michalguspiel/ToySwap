package com.erdees.toyswap.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.erdees.toyswap.model.firebaseAuth.AuthDao
import com.erdees.toyswap.model.firebaseAuth.AuthRepository
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential

class ReAuthViewModel(application: Application): AndroidViewModel(application) {

    private val authRepository : AuthRepository

    init {
        val authDao = AuthDao.getInstance(application)
        authRepository = AuthRepository(authDao)
    }

    fun reAuthenticate(cred: AuthCredential): Task<Void>?{
      return  authRepository.reAuthenticate(cred)
    }

}