package com.erdees.toyswap.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.erdees.toyswap.model.firebaseAuth.AuthUserDao
import com.erdees.toyswap.model.firebaseAuth.AuthUserRepository
import com.google.android.gms.tasks.Task

class ChangeNameDialogViewModel (application: Application) : AndroidViewModel(application) {

    private val authUserRepository : AuthUserRepository

    init {
        val authDao = AuthUserDao.getInstance(application)
        authUserRepository = AuthUserRepository(authDao)
    }

    fun updateNames(firstName: String , lastName: String): Task<Void>? {
         return authUserRepository.updateNames(firstName,lastName)
    }

    val clientUserLiveData = authUserRepository.getClientUserLiveData()


}