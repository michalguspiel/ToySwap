package com.erdees.toyswap.model.models

data class ClientUser(
    val firstName: String = "",
    val lastName : String = "",
    val emailAddress: String = "",
    val points: Long = 0L,
    val reputation : Long = 0L,
    val avatar: String = "",
    val addressCity: String = "",
    val addressPostCode: String = "",
    val addressStreet: String = ""
) {

}