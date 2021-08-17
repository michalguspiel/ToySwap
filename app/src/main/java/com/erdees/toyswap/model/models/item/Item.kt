package com.erdees.toyswap.model.models.item

import com.erdees.toyswap.model.models.item.itemCategory.ItemCategory
import com.erdees.toyswap.model.models.item.itemPickupOption.PickupOption
import com.google.firebase.Timestamp

data class Item(
    val name: String = "",
    val category: ItemCategory = ItemCategory(0,null,"",""),
    val description: String = "",
    val size : String = "",
    val itemCondition: String = "",
    val price: Double = 0.0,
    val pickupOptions : List<PickupOption> = listOf(),
    val deliveryCost: Double? = null,
    val mainImageUrl: String = "",
    val otherImagesUrl: List<String> = listOf(),
    val isItemAvailable: Boolean = true,
    val timeStamp: Timestamp = Timestamp.now(),
    val userId: String = "",
) {

}