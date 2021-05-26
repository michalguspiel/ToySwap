package com.erdees.toyswap.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.erdees.toyswap.model.Address
import com.erdees.toyswap.model.repositories.AuthRepository
import com.google.android.gms.tasks.Task

class ChangeAddressDialogViewModel(application: Application): AndroidViewModel(application) {

    private val authRepository : AuthRepository = AuthRepository.getInstance(application)

    val userLiveData = authRepository.getUserLiveData()

    val userAddressLiveData = authRepository.getUserAddressLiveData()

    fun updateAddress(address: Address): Task<Void>? {
           return authRepository.updateAddress(address)
    }

}