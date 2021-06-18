package com.erdees.toyswap.viewModel

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.erdees.toyswap.model.firebaseQuery.ItemDao
import com.erdees.toyswap.model.firebaseQuery.ItemRepository
import com.erdees.toyswap.model.models.item.Item
import com.erdees.toyswap.model.models.item.ItemCategory
import com.erdees.toyswap.model.repositories.CategoryRepository
import com.erdees.toyswap.model.repositories.PicturesRepository
import com.google.android.gms.tasks.Task
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference

class AddItemFragmentViewModel : ViewModel() {

    private val categoryRepository = CategoryRepository.getInstance()

    private val picturesRepository = PicturesRepository.getInstance()

    private var itemRepository: ItemRepository

    init {
        val itemDao = ItemDao.getInstance()
        itemRepository = ItemRepository(itemDao)
    }

    val categoryLiveData = categoryRepository.getCategoryLiveData()

    fun addPicture(uri: Uri) = picturesRepository.addPicture(uri)

    fun removePicture(uri: Uri) = picturesRepository.removePicture(uri)

    fun rearrangePictures(indexOfElementToMove: Int, finalIndexOfElement: Int) =
        picturesRepository.rearrangePictures(indexOfElementToMove, finalIndexOfElement)

    fun clearPicturesData() = picturesRepository.clearPicturesData()

    val picturesLiveData = picturesRepository.getPicturesLiveData()

    fun addPicUri(uri: Uri) = picturesRepository.addPicUri(uri)

    fun getUriOfPicsInCloudLiveData() = picturesRepository.getUriOfPicsInCloudLiveData()

    fun addPicturesToCloud() = picturesRepository.addPicturesToCloud()

    fun addItemToFirebase(
        name: String,
        category: ItemCategory,
        desc: String,
        price: Double,
        mainImage: String?,
        otherImages: List<String>?,
        timeStamp: Timestamp,
        userId: String
    ) : Task<DocumentReference> = itemRepository.addItemToFirebase(Item(name,
        category.documentRef(),
        desc,
        price,
        getUriOfPicsInCloudLiveData().value!!.first().toString(),
        getUriOfPicsInCloudLiveData().value!!.drop(1).map {it.toString()},
        timeStamp,
        userId))


}