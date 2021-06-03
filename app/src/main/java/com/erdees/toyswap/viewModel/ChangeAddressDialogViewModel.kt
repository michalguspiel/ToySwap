package com.erdees.toyswap.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.erdees.toyswap.model.models.Address
import com.erdees.toyswap.model.firebaseAuth.AuthDao
import com.erdees.toyswap.model.firebaseAuth.AuthRepository
import com.google.android.gms.tasks.Task

class ChangeAddressDialogViewModel(application: Application): AndroidViewModel(application) {


    private val authRepository : AuthRepository

    init {
        val authDao = AuthDao.getInstance(application)
        authRepository = AuthRepository(authDao)
    }


    val userLiveData = authRepository.getUserLiveData()

    val clientUserLiveData = authRepository.getClientUserLiveData()


    fun updateAddress(address: Address): Task<Void>? {
           return authRepository.updateAddress(address)
    }

}