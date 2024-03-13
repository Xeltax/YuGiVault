package com.example.yugivault.utils.DAO

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.yugivault.utils.Entity.Card

@Dao
interface CardDAO {
    @Query("SELECT * FROM card")
    fun getAll(): List<Card>
    @Query("SELECT * FROM card WHERE id = :id")
    fun getById(id: Int): Card
    @Query("SELECT * FROM card WHERE name = :name")
    fun getByName(name: String): Card
    @Insert
    fun insert(card: Card)
    @Delete
    fun delete(card: Card)
    @Update
    fun update(card: Card)
}