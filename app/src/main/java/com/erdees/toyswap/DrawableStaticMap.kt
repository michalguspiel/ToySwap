package com.erdees.toyswap

import com.erdees.toyswap.model.models.item.itemCategory.ItemCategory
/**This is map of icons, tried to avoid hard coding as much as possible although this must be hard coded
 * since ID's of drawables might change with each compilation and insertion of new icons. Each [ItemCategory] item in SQLite has
 * reference to key in this [DrawableStaticMap] from which then takes ID of icon drawable.*/
object DrawableStaticMap {

    val map: Map<String,Int> = mapOf(
        Pair("sports", R.drawable.ic_baseline_sports_24),
        Pair("toys",R.drawable.ic_baseline_home_24)
    )
}