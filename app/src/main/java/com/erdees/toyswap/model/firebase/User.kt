package com.erdees.toyswap.model.firebase

data class User(
    val name: String = "",
    val emailAddress: String = "",
    val points: Long = 0L,
    val avatar: String = "",
    val addressCity: String = "",
    val addressPostCode: String = "",
    val addressStreet: String = ""
) {

}