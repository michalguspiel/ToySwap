package com.erdees.toyswap.model.firebase

data class ClientUser(
    val firstName: String = "",
    val lastName : String = "",
    val emailAddress: String = "",
    val points: Long = 0L,
    val avatar: String = "",
    val addressCity: String = "",
    val addressPostCode: String = "",
    val addressStreet: String = ""
) {

}