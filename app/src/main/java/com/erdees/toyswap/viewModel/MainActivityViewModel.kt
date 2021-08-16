package com.erdees.toyswap.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.erdees.toyswap.model.firebaseAuth.AuthUserDao
import com.erdees.toyswap.model.firebaseAuth.AuthUserRepository
import com.erdees.toyswap.model.repositories.CategoryRepository

class MainActivityViewModel(application: Application): AndroidViewModel(application) {

    private val authUserRepository : AuthUserRepository

    private val categoryRepository = CategoryRepository.getInstance(application)

    init {
        val authDao = AuthUserDao.getInstance(application)
        authUserRepository = AuthUserRepository(authDao)

    }



    val userLiveData = authUserRepository.getUserLiveData()
    val isUserLoggedOut = authUserRepository.getIsUserLoggedOutLiveData()
}