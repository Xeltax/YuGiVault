package com.example.yugivault.utils.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.yugivault.utils.entity.Card
import com.example.yugivault.utils.entity.Deck
import com.example.yugivault.utils.entity.DeckCardCrossRef
import com.example.yugivault.utils.entity.DeckWithCard

@Dao
interface DeckDAO {
    @Query("SELECT * FROM deck")
    fun getALLDecks():List<Deck>

    @Transaction
    @Query("SELECT * FROM deck")
    fun getALLDecksWithCards(): List<DeckWithCard>

    @Transaction
    @Query("SELECT * FROM deck WHERE deckId =:deckId")
    fun getDeckWithCards(deckId:Int):DeckWithCard

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(deck: Deck)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCard(card: Card)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDeckCardCrossRef(crossRef: DeckCardCrossRef)

    @Query("DELETE FROM DeckCardCrossRef WHERE deckId IN (:deckIds)")
    suspend fun deleteDeckWithCArd(deckIds: List<Int>)

    @Query("DELETE FROM deck WHERE deckId IN (:deckIds)")
    suspend fun deleteDeck(deckIds: List<Int>)



}