package com.example.yugivault.utils.entity

import androidx.room.Entity
@Entity(primaryKeys = ["deckId","uid"])
data class DeckCardCrossRef(
    val deckId: Int,
    val uid: Int
)
