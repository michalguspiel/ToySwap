package com.erdees.toyswap.model.firebaseQuery

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.erdees.toyswap.model.models.user.PublicUserData
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SellerDao {

    private var sellerListLiveData: MutableLiveData<List<PublicUserData?>> = MutableLiveData()

    private var sellerList = mutableListOf<PublicUserData?>()


    private val db = Firebase.firestore

    fun getSellerData(sellerId: String): PublicUserData? {
        var sellerWithThisId: PublicUserData?
        if (sellerListLiveData.value?.any { it?.docId == sellerId } == true) {
       sellerWithThisId = sellerListLiveData.value?.first { it?.docId == sellerId }!!
            return sellerWithThisId
        } else return null
    }


     fun downloadSellerFromServer(sellerId: String): Task<DocumentSnapshot> {
       val collection =  db.collection("usersPublicInfo").document(sellerId)
         return  collection.get().addOnSuccessListener {
            if (it != null) {
                sellerList.add(it.toObject(PublicUserData::class.java))
                sellerListLiveData.value = sellerList
            } else {
                Log.d(TAG, "No such document")
            }
        }
             .addOnFailureListener {
             }
    }

    /**SINGLETON*/
    companion object {
        const val TAG = "SellerDao"

        @Volatile
        private var instance: SellerDao? = null
        fun getInstance() =
            instance ?: synchronized(this) {
                instance
                    ?: SellerDao().also { instance = it }
            }
    }
}
