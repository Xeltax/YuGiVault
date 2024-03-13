package com.example.yugivault.utils.DataBase

import androidx.room.Database
import com.example.yugivault.utils.DAO.CardDAO
import com.example.yugivault.utils.DAO.DeckDAO
import com.example.yugivault.utils.Entity.Card
import com.example.yugivault.utils.Entity.Deck

@Database(entities = [Card::class,Deck::class], version = 1)
abstract class VaultDB {
    abstract fun cardDAO(): CardDAO
    abstract fun deckDAO(): DeckDAO

}