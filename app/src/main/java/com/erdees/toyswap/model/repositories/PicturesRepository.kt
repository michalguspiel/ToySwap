package com.erdees.toyswap.model.repositories

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.erdees.toyswap.model.CustomListRearranger.rearrange

class PicturesRepository {

    private var pictures = mutableListOf<Uri>()
    private val picturesLiveData : MutableLiveData<List<Uri>> = MutableLiveData()

    init {
        picturesLiveData.value = pictures
    }

    fun getPicturesLiveData() = picturesLiveData

    fun addPicture(uri: Uri) {
        pictures.add(uri)
        picturesLiveData.value = pictures
    }

    fun removePicture(uri: Uri){
        pictures.remove(uri)
        picturesLiveData.value = pictures
    }

    fun clearPicturesData(){
        pictures.clear()
        picturesLiveData.value = pictures
    }

    fun rearrangePictures(indexOfElementToMove: Int, finalIndexOfElement: Int)  {
       val rearrangeList = pictures.rearrange(indexOfElementToMove,finalIndexOfElement) as MutableList<Uri>
        pictures = rearrangeList
        picturesLiveData.value = pictures
    }

    /**Singleton*/
    companion object {
        @Volatile
        private var instance: PicturesRepository? = null
        fun getInstance() = instance ?:  synchronized(this) {
            instance
                ?: PicturesRepository().also {  instance = it}
        }
    }

}