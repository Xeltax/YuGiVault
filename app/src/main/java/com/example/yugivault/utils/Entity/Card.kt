package com.example.yugivault.utils.Entity

data class Card(
    val id: Int,
    val name: String,
    val type: String,
    val frameType: String,
    val description: String,
    val atk: Int,
    val def: Int,
    val level: Int,
    val race: String,
    val attribute: String,
    val artwork: String //  url -> id.jpg a aller chercher dans futur dossier img

)

object CardContract{
    const val TABLE_NAME = "cards"
    object Columns {
        const val ID = "id"
        const val NAME = "name"
        const val TYPE = "type"
        const val FRAMETYPE = "frameType"
        const val DESCRIPTION = "description"
        const val ATK = "atk"
        const val DEF = "def"
        const val LEVEL = "level"
        const val RACE = "race"
        const val ATTRIBUTE = "attribute"
        const val ARTWORK = "artwork"
    }

}

