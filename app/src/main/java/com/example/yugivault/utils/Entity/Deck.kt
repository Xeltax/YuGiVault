package com.example.yugivault.utils.Entity

import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity
data class Deck(
    @PrimaryKey val id: Int,
    val name: String,
    val description: String,
    val cards: List<Card>,
    val favorite: Boolean
)
