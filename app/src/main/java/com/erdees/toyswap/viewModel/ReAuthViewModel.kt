package com.erdees.toyswap.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.erdees.toyswap.model.firebaseAuth.AuthUserDao
import com.erdees.toyswap.model.firebaseAuth.AuthUserRepository
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential

class ReAuthViewModel(application: Application): AndroidViewModel(application) {

    private val authUserRepository : AuthUserRepository

    init {
        val authDao = AuthUserDao.getInstance(application)
        authUserRepository = AuthUserRepository(authDao)
    }

    fun reAuthenticate(cred: AuthCredential): Task<Void>?{
      return  authUserRepository.reAuthenticate(cred)
    }

}