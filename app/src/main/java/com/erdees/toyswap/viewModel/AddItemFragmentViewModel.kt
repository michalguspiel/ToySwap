package com.erdees.toyswap.viewModel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import com.erdees.toyswap.model.firebaseAuth.AuthUserDao
import com.erdees.toyswap.model.firebaseAuth.AuthUserRepository
import com.erdees.toyswap.model.firebaseQuery.ItemDao
import com.erdees.toyswap.model.firebaseQuery.ItemRepository
import com.erdees.toyswap.model.models.item.Item
import com.erdees.toyswap.model.models.item.PickupOption
import com.erdees.toyswap.model.repositories.CategoryRepository
import com.erdees.toyswap.model.repositories.PicturesRepository
import com.google.android.gms.tasks.Task
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference

class AddItemFragmentViewModel(application: Application) : AndroidViewModel(application) {

    private val categoryRepository = CategoryRepository.getInstance()

    private val picturesRepository = PicturesRepository.getInstance()

    private var itemRepository: ItemRepository

    private var authUserRepository: AuthUserRepository


    init {
        val itemDao = ItemDao.getInstance()
        itemRepository = ItemRepository(itemDao)
        val authDao = AuthUserDao.getInstance(application)
        authUserRepository = AuthUserRepository(authDao)

    }

    fun getUserId() = authUserRepository.getUserLiveData().value?.uid

    val categoryLiveData = categoryRepository.getCategoryLiveData()

    fun clearCategory() = categoryRepository.clearCategory()

    fun addPicture(uri: Uri) = picturesRepository.addPicture(uri)

    fun removePicture(uri: Uri) = picturesRepository.removePicture(uri)

    fun rearrangePictures(indexOfElementToMove: Int, finalIndexOfElement: Int) =
        picturesRepository.rearrangePictures(indexOfElementToMove, finalIndexOfElement)

    fun clearPicturesData() = picturesRepository.clearPicturesData()

    val picturesLiveData = picturesRepository.getPicturesLiveData()

    fun getUriOfPicsInCloudLiveData() = picturesRepository.getUriOfPicsInCloudLiveData()

    fun addPicturesToCloud() = picturesRepository.addPicturesToCloud()

    fun addItemToFirebase(
        itemName: String,
        itemDesc: String,
        itemPrice: Double,
        deliveryPrice: Double?,
        itemCondition : String,
        itemSize: String,
        pickupOptions:List<PickupOption>
    ): Task<DocumentReference> {
        val mainPic =
            getUriOfPicsInCloudLiveData().value?.first { it.first == 0 }?.second.toString() // MAIN PIC MUST EXIST
        val otherPics =
            getUriOfPicsInCloudLiveData().value!!.filter { it.first != 0 }.sortedBy { it.first }
                .map { it.second.toString() }
        val category = categoryLiveData.value!!
        val userId = getUserId()
        return itemRepository.addItemToFirebase(
            Item(
                itemName,
                category.toFirebaseItemCategory(),
                itemDesc,
                itemSize,
                itemCondition,
                itemPrice,
                pickupOptions,
                deliveryPrice,
                mainPic,
                otherPics,
                true,
                Timestamp.now(),
                userId!!
            )
        )
    }

    fun setItemToPresent(item: Item) = itemRepository.setItemToPresent(item)

}