package com.example.yugivault

import android.app.Application
import com.example.yugivault.utils.DataBase.VaultDB
import androidx.room.Room

class Vault : Application() {
    companion object {
        private lateinit var instance: VaultDB

        fun getDatabase(): VaultDB {
            return instance
        }
    }

    override fun onCreate() {
        super.onCreate()
        val db = Room.databaseBuilder(applicationContext, VaultDB::class.java, "Vault_db").build()
        instance = db
    }
}