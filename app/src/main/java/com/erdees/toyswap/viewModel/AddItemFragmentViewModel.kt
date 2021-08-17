package com.erdees.toyswap.viewModel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import com.erdees.toyswap.model.firebaseAuth.AuthUserDao
import com.erdees.toyswap.model.firebaseAuth.AuthUserRepository
import com.erdees.toyswap.model.firebaseQueries.item.ItemDao
import com.erdees.toyswap.model.firebaseQueries.item.ItemRepository
import com.erdees.toyswap.model.localDatabase.ItemCategoryRepository
import com.erdees.toyswap.model.localDatabase.LocalDatabase
import com.erdees.toyswap.model.localDatabase.LocalRepository
import com.erdees.toyswap.model.models.item.Item
import com.erdees.toyswap.model.models.item.itemPickupOption.PickupOption
import com.erdees.toyswap.model.repositories.CategoryRepository
import com.erdees.toyswap.model.repositories.PicturesRepository
import com.google.android.gms.tasks.Task
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference

class AddItemFragmentViewModel(application: Application) : AndroidViewModel(application) {

    private var itemCategoryRepository : ItemCategoryRepository

    private var categoryRepository : CategoryRepository

    private val picturesRepository = PicturesRepository.getInstance()

    private var itemRepository: ItemRepository

    private var authUserRepository: AuthUserRepository

    private var localDatabaseRepository : LocalRepository

    init {
        val localDatabaseDao = LocalDatabase.getDatabase(application).itemConditionDao()
        val itemDao = ItemDao.getInstance()
        val itemCategoryDao = LocalDatabase.getDatabase(application).itemCategoryDao()
        val authDao = AuthUserDao.getInstance(application)
        itemRepository = ItemRepository(itemDao)
        itemCategoryRepository = ItemCategoryRepository(itemCategoryDao)
        authUserRepository = AuthUserRepository(authDao)
        localDatabaseRepository = LocalRepository(localDatabaseDao)
        categoryRepository = CategoryRepository.getInstance(itemCategoryRepository)
    }

    private fun getUserId() = authUserRepository.getUserLiveData().value?.uid

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
                category,// TODO MAKE SURE ITS CORRECT / DOESN'T NEED ANOTHER IMPLEMENTATION
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

    fun getItemConditions() = localDatabaseRepository.getItemConditions()

}