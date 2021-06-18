package com.erdees.toyswap.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.erdees.toyswap.model.firebaseAuth.AuthDao
import com.erdees.toyswap.model.firebaseAuth.AuthRepository

class AddItemAndroidViewModel(application: Application): AndroidViewModel(application) {

    private var authRepository : AuthRepository
    init {
        val dao = AuthDao.getInstance(application)
        authRepository = AuthRepository(dao)
    }

    fun getUserId() = authRepository.getUserLiveData().value?.uid

}