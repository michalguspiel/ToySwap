package com.erdees.toyswap.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.erdees.toyswap.model.Registration
import com.erdees.toyswap.model.firebase.AuthDao
import com.erdees.toyswap.model.firebase.AuthRepository
import com.google.android.gms.tasks.Task

class ChangePasswordDialogViewModel(application: Application): AndroidViewModel(application) {

    private val authRepository: AuthRepository

    init {
        val authDao = AuthDao.getInstance(application)
        authRepository = AuthRepository(authDao)
    }

    val userLiveData = authRepository.getUserLiveData()


    fun changePassword(registration: Registration): Task<Void>?{
       return authRepository.changePassword(registration)
    }
}