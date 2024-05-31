package com.example.yugivault.utils.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "deck")
data class Deck(
    @PrimaryKey val deckId: Int,
    @ColumnInfo(name = "name") val name: String,
)
