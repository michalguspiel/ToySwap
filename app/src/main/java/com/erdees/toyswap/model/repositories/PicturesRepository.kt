package com.erdees.toyswap.model.repositories

import android.net.Uri
import androidx.lifecycle.MutableLiveData

class PicturesRepository {

    private val pictures = mutableListOf<Uri>()
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