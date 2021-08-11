package com.erdees.toyswap.model.localDatabase

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "item_conditions")
data class ItemCondition(
    @PrimaryKey val id: Int?,
    val name: String
)
