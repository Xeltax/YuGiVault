package com.example.yugivault.utils.DAO

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.yugivault.utils.Entity.Deck

@Dao
interface DeckDAO {
    @Query("SELECT * FROM deck")
    fun getAll(): List<Deck>
    @Query("SELECT * FROM deck WHERE id = :id")
    fun getById(id: Int): Deck
    @Query("SELECT * FROM deck WHERE name = :name")
    fun getByName(name: String): Deck
    @Insert
    fun insert(deck: Deck)
    @Delete
    fun delete(deck: Deck)
    @Update
    fun update(deck: Deck)
}