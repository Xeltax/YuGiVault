package com.example.yugivault.utils.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.yugivault.utils.entity.Card

@Dao
interface CardDAO {
    @Query("SELECT * FROM card")
    fun getCards(): LiveData<List<Card>>

    @Query("SELECT * FROM card WHERE uid = :uid")
    fun getCardById(uid: Int): LiveData<Card>


    @Insert
    suspend fun insertAll(vararg cards: Card)

    @Insert
    suspend fun insert(card: Card)

    @Delete
    suspend fun delete(card: Card)


}