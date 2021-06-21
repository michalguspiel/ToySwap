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

    fun getUriOfPicsInCloudLiveData() = picturesRepository.getUriOfPicsInCloudLiveData()

    fun addPicturesToCloud() = picturesRepository.addPicturesToCloud()

    fun addItemToFirebase(
        name: String,
        category: ItemCategory,
        desc: String,
        price: Double,
        timeStamp: Timestamp,
        userId: String
    ) : Task<DocumentReference> {
        val mainPic =
            getUriOfPicsInCloudLiveData().value?.first { it.first == 0 }?.second.toString() // MAIN PIC MUST EXIST
        val otherPics = getUriOfPicsInCloudLiveData().value!!.filter{it.first != 0}.sortedBy{it.first}.map {it.second.toString()}
       return itemRepository.addItemToFirebase(Item(name,
            category.documentRef(),
            desc,
            price,
            mainPic,
            otherPics,
            timeStamp,
            userId))
    }
}