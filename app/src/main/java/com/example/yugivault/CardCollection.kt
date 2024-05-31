package com.example.yugivault

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.yugivault.utils.db.AppDatabase
import com.example.yugivault.utils.entity.Card
import com.example.yugivault.utils.view.CardAdapter
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

fun populateDatabase(db: AppDatabase) {
    val cardDAO = db.cardDAO()

    // Create dummy data
    val card1 = Card(1, "Dark Magician", "Spellcaster", "The ultimate wizard.", 2500, 2100, 7, "Dark", "Spellcaster")
    val card2 = Card(2, "Blue-Eyes White Dragon", "Dragon", "This legendary dragon is a powerful engine of destruction.", 3000, 2500, 8, "Light", "Dragon")
    val card3 = Card(3, "Red-Eyes Black Dragon", "Dragon", "A ferocious dragon with a deadly attack.", 2400, 2000, 7, "Dark", "Dragon")

    // Insert dummy data into the database
    GlobalScope.launch {
        cardDAO.insert(card1)
        cardDAO.insert(card2)
        cardDAO.insert(card3)
    }
}
class CardCollection : ComponentActivity() {


    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CardAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_card_collection)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)


        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "dbVault"
        ).build()

        val cardDAO = db.cardDAO()

        //populateDatabase(db)


        val cards = cardDAO.getCards()
        println(cards.value.toString())
        cards.observe(this,{cards ->
            cards?.let{
                if (it.isNotEmpty()){
                    adapter = CardAdapter(it,1)
                    recyclerView.adapter = adapter
                }
            }
        })

    }

    fun startCardDetailActivity(card: Card) {
        println("CARTE  dans func = "+card)
        val intent = Intent(this, CardDetailActivity::class.java)
        intent.putExtra("CARD_ID", card.uid) // Passer l'identifiant de la carte ou toute autre donnée nécessaire
        startActivity(intent)
        finish()
    }


}