package com.erdees.toyswap.model.models.user

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId

data class PublicUserData (
    @DocumentId val docId: String = "",
    val firstName: String = "",
    val avatar: String = "",
    val addressCity: String = "",
    val reputation : Long = 0L,
    val accCreationTimeStamp : Timestamp = Timestamp.now()
){}