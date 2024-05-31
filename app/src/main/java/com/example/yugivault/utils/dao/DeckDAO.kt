package com.example.yugivault.utils.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.example.yugivault.utils.entity.Deck
import com.example.yugivault.utils.entity.DeckWithCard

@Dao
interface DeckDAO {
    @Query("SELECT * FROM deck")
    fun getALLDecks():List<Deck>

    @Transaction
    @Query("SELECT * FROM deck")
    fun getDecksWIthCards(): List<DeckWithCard>

}