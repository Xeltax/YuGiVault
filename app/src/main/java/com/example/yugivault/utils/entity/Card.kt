package com.example.yugivault.utils.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "card")
data class Card(
    @PrimaryKey val uid: Int,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "type") val type: String,
    @ColumnInfo(name = "frameType") val frameType: String,
    @ColumnInfo(name = "desc") val desc: String,
    @ColumnInfo(name = "atk") val atk: Int,
    @ColumnInfo(name = "def") val def: Int,
    @ColumnInfo(name = "level") val level: Int,
    @ColumnInfo(name = "race") val race: String,
    @ColumnInfo(name = "attribute") val attribute: String,
    @ColumnInfo(name = "archetype") val archetype: String? = null,
    @ColumnInfo(name = "card_sets") val card_sets: String? = null,
    @ColumnInfo(name = "card_images") val card_images: String? = null,
    @ColumnInfo(name = "card_prices") val card_prices: String? = null

)
