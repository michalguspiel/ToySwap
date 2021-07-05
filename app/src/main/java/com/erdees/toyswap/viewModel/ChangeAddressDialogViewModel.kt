package com.erdees.toyswap.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.erdees.toyswap.model.firebaseAuth.AuthUserDao
import com.erdees.toyswap.model.firebaseAuth.AuthUserRepository
import com.erdees.toyswap.model.models.Address
import com.google.android.gms.tasks.Task

class ChangeAddressDialogViewModel(application: Application): AndroidViewModel(application) {


    private val authUserRepository : AuthUserRepository

    init {
        val authDao = AuthUserDao.getInstance(application)
        authUserRepository = AuthUserRepository(authDao)
    }


    val userLiveData = authUserRepository.getUserLiveData()

    val clientUserLiveData = authUserRepository.getClientUserLiveData()


    fun updateAddress(address: Address): Task<Void>? {
           return authUserRepository.updateAddress(address)
    }


}