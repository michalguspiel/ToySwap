package com.erdees.toyswap.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.erdees.toyswap.model.Registration
import com.erdees.toyswap.model.firebaseAuth.AuthUserDao
import com.erdees.toyswap.model.firebaseAuth.AuthUserRepository
import com.google.android.gms.tasks.Task

class ChangePasswordDialogViewModel(application: Application): AndroidViewModel(application) {

    private val authUserRepository: AuthUserRepository

    init {
        val authDao = AuthUserDao.getInstance(application)
        authUserRepository = AuthUserRepository(authDao)
    }

    val userLiveData = authUserRepository.getUserLiveData()


    fun changePassword(registration: Registration): Task<Void>?{
       return authUserRepository.changePassword(registration)
    }
}