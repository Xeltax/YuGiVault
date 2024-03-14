package com.example.yugivault.utils.DAO

import com.example.yugivault.utils.DataBase.Vault
import com.example.yugivault.utils.Entity.Card
import java.util.Random

object CardDAO {
    fun addCard(dbHelper : Vault, card: Card) {
        val db = dbHelper.writableDatabase
        db.execSQL("INSERT INTO Card (id, name, type, frameType, description, atk, def,race, attribute, artwork) VALUES " +
                "(${card.id}, '${card.name}', '${card.type}', '${card.frameType}', '${card.description}', ${card.atk}, ${card.def}, ${card.level}, '${card.race}'," +
                " '${card.attribute}', '${card.artwork}')")
        db.close()
    }

    fun getCard(dbHelper : Vault, id: Int): Card? {
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM Card WHERE id = $id", null)
        if (cursor.moveToNext()) {
            val card = Card(
                cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                cursor.getString(cursor.getColumnIndexOrThrow("name")),
                cursor.getString(cursor.getColumnIndexOrThrow("type")),
                cursor.getString(cursor.getColumnIndexOrThrow("frameType")),
                cursor.getString(cursor.getColumnIndexOrThrow("description")),
                cursor.getInt(cursor.getColumnIndexOrThrow("atk")),
                cursor.getInt(cursor.getColumnIndexOrThrow("def")),
                cursor.getInt(cursor.getColumnIndexOrThrow("level")),
                cursor.getString(cursor.getColumnIndexOrThrow("race")),
                cursor.getString(cursor.getColumnIndexOrThrow("attribute")),
                cursor.getString(cursor.getColumnIndexOrThrow("artwork")))
                db.close()
                return card
        }
        return null
    }

    fun generateRandomCard(): Card {
        val random = Random()
        val id = random.nextInt(1000) // ID aléatoire
        val name = "Card ${id}" // Nom aléatoire
        val type = "Type ${random.nextInt(5)}" // Type aléatoire
        val frameType = "Frame ${random.nextInt(3)}" // FrameType aléatoire
        val description = "Description for card ${id}" // Description aléatoire
        val atk = random.nextInt(100) // Attaque aléatoire
        val def = random.nextInt(100) // Défense aléatoire
        val level = random.nextInt(10) // Niveau aléatoire
        val race = "cera"
        val attribute = "attribu"
        val artwork = "path/to/image/.png" // Chemin de l'image aléatoire

        return Card(id, name, type, frameType, description, atk, def, level, race,attribute,artwork)
    }
}