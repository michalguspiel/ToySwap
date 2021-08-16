package com.erdees.toyswap.model

import com.erdees.toyswap.model.models.item.itemPickupOption.PickupOption

object Constants {



    val shipment = PickupOption("Shipment")
    val personalPickup = PickupOption("Personal pickup")

    const val STARTING_CATEGORY_ID = 0

    const val REQUEST_CODE = 149
    const val RC_SIGN_IN = 21415
    const val NO_PROFILE_PICTURE_URL = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcShaBYmwqvTkkSv8HbjMk813wFS8TU-0ovKmA&usqp=CAU"
}