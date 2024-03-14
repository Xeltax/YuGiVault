package com.example.yugivault.utils.DataBase

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.yugivault.utils.DAO.CardDAO
import com.example.yugivault.utils.Entity.Card

class Vault(context: Context): SQLiteOpenHelper(context, "vault.db", null, 1) {
    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = """
        CREATE TABLE Card (
            id INTEGER PRIMARY KEY,
            name TEXT,
            type TEXT,
            frameType TEXT,
            description TEXT,
            atk INTEGER,
            def INTEGER,
            level TEXT,
            race TEXT,
            attribute TEXT,
            artwork TEXT
        )
    """.trimIndent()
        db.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }

    fun addCard(card: Card) {
        CardDAO.addCard(this, card)
    }
}
