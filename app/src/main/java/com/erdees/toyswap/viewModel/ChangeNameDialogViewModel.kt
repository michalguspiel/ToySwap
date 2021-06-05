package com.erdees.toyswap.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.erdees.toyswap.model.firebaseAuth.AuthDao
import com.erdees.toyswap.model.firebaseAuth.AuthRepository
import com.google.android.gms.tasks.Task

class ChangeNameDialogViewModel (application: Application) : AndroidViewModel(application) {

    private val authRepository : AuthRepository

    init {
        val authDao = AuthDao.getInstance(application)
        authRepository = AuthRepository(authDao)
    }

    fun updateNames(firstName: String , lastName: String): Task<Void>? {
         return authRepository.updateNames(firstName,lastName)
    }

    val clientUserLiveData = authRepository.getClientUserLiveData()


}