package com.erdees.toyswap.model.repositories

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.erdees.toyswap.model.CustomListRearranger.rearrange
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import java.util.*

class PicturesRepository {

    private var pictures = mutableListOf<Uri>()
    private val picturesLiveData : MutableLiveData<List<Uri>> = MutableLiveData()

    private var uriOfPicsInCloud = mutableListOf<Pair<Int,Uri>>()
    private val uriOfPicsInCloudLive : MutableLiveData<List<Pair<Int,Uri>>> = MutableLiveData()


    private val firebaseStorage : FirebaseStorage = Firebase.storage
    private val storageRef = firebaseStorage.reference
    private val itemsPictureRef = storageRef.child("itemPictures/")

    init {
        picturesLiveData.value = pictures
        uriOfPicsInCloudLive.value = uriOfPicsInCloud
    }


    fun addPicturesToCloud() {
        picturesLiveData.value?.forEach { eachPicUri ->
            val indexOfThisPic = picturesLiveData.value!!.indexOf(eachPicUri)
            val generatedPicId = UUID.randomUUID()
            val docRef = itemsPictureRef.child(generatedPicId.toString())
            docRef.putFile(eachPicUri).addOnSuccessListener {
                Log.i(TAG,"1stSuccess")
                docRef.downloadUrl.addOnSuccessListener {  uri ->
                    Log.i(TAG,"2ndSuccess")
                    addPicUri(indexOfThisPic,uri)
                }
                    .addOnFailureListener { Log.i(TAG,"FAIL DOWNLOAD URI") }
            }.addOnFailureListener { Log.i(TAG,"ERRRROR") }
        }
    }

    fun getUriOfPicsInCloudLiveData() = uriOfPicsInCloudLive

    private fun addPicUri(indexOfThisPic : Int,uri: Uri){
        uriOfPicsInCloud.add(Pair(indexOfThisPic,uri))
        uriOfPicsInCloudLive.value = uriOfPicsInCloud
    }

    private fun clearUriOfPicsInCloudData(){
        uriOfPicsInCloud.clear()
        uriOfPicsInCloudLive.value = uriOfPicsInCloud
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
        clearUriOfPicsInCloudData()
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
        const val TAG = "PicRepo"
    }

}