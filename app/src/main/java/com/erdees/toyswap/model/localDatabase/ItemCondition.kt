package com.erdees.toyswap.model.localDatabase

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ItemCondition(
    @PrimaryKey val id: Long,
    val name: String
)
