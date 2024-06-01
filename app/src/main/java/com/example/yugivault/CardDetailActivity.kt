package com.example.yugivault

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.yugivault.utils.db.AppDatabase
import com.example.yugivault.utils.view.CardAdapter

class CardDetailActivity : ComponentActivity(){

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CardAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_card)
        Toast.makeText(this, "CardDetailActivity", Toast.LENGTH_SHORT).show()

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "dbVault"
        ).build()

        val cardDAO = db.cardDAO()

        val cardId = intent.getIntExtra("CARD_ID", -1)
        println("Valeurr de l'intent "+cardId)

        if(cardId !=-1){
            println("on est dedans ")
            val card = cardDAO.getCardById(cardId)

            card.observe(this, { card ->
                println("Obs : $card")
                card?.let {
                    adapter = CardAdapter(listOf(card), 2, this)
                    recyclerView.adapter = adapter
                }
            })
        }
    }

}