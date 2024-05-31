package com.example.yugivault.utils.entity

import androidx.lifecycle.LiveData
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Junction
import androidx.room.Relation

data class DeckWithCard(
    @Embedded val deck : Deck,
    @Relation(
        parentColumn = "deckId",
        entityColumn = "uid",
        associateBy = Junction(DeckCardCrossRef::class)
    )
    val cards: List<Card>
)
