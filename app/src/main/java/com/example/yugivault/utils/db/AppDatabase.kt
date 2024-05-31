package com.example.yugivault.utils.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.yugivault.utils.dao.CardDAO
import com.example.yugivault.utils.dao.DeckDAO
import com.example.yugivault.utils.entity.Card
import com.example.yugivault.utils.entity.Deck
import com.example.yugivault.utils.entity.DeckCardCrossRef

@Database(entities = [Card::class,Deck::class,DeckCardCrossRef::class], version = 3)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cardDAO(): CardDAO
    abstract fun deckDAO():DeckDAO


}