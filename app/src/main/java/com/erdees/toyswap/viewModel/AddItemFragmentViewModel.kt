package com.erdees.toyswap.viewModel

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.erdees.toyswap.model.repositories.CategoryRepository
import com.erdees.toyswap.model.repositories.PicturesRepository

class AddItemFragmentViewModel: ViewModel() {

    private val categoryRepository = CategoryRepository.getInstance()

    private val picturesRepository = PicturesRepository.getInstance()

    val categoryLiveData = categoryRepository.getCategoryLiveData()

    fun addPicture(uri: Uri) = picturesRepository.addPicture(uri)

    fun removePicture(uri: Uri) = picturesRepository.removePicture(uri)

    fun clearPicturesData() = picturesRepository.clearPicturesData()

    val picturesLiveData = picturesRepository.getPicturesLiveData()


}