package com.erdees.toyswap.model.firebaseQuery

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot

class SellerRepository (private val dao : SellerDao){

    fun getSellerData(sellerId: String) = dao.getSellerData(sellerId)

    fun downloadSellerFromServer(sellerId: String) : Task<DocumentSnapshot> = dao.downloadSellerFromServer(sellerId)


}