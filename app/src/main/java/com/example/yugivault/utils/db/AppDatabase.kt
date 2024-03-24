package com.example.yugivault.utils.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.yugivault.utils.dao.CardDAO
import com.example.yugivault.utils.entity.Card

@Database(entities = [Card::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cardDAO(): CardDAO


}