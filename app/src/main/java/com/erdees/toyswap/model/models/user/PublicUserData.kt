package com.erdees.toyswap.model.models.user

import com.google.firebase.Timestamp

data class PublicUserData (
    val firstName: String = "",
    val avatar: String = "",
    val addressCity: String = "",
    val reputation : Long = 0L,
    val accCreationTimeStamp : Timestamp = Timestamp.now()
){}