package com.example.yugivault.utils.db

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.yugivault.utils.dao.CardDAO
import com.example.yugivault.utils.entity.Card

@Database(entities = [Card::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cardDAO(): CardDAO


}