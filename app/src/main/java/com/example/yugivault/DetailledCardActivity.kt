package com.example.yugivault

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.yugivault.utils.db.AppDatabase
import com.example.yugivault.utils.entity.Card
import com.example.yugivault.utils.view.CardAdapter

class DetailledCardActivity: ComponentActivity(){

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CardAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_card_collection)
        println("DetailledCardActivity")

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)


        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "dbVault"
        ).build()

        val cardDAO = db.cardDAO()


        val intent = intent
        val cardId = intent.getIntExtra("CARD_ID", -1)
        println("Card ID : $cardId")
        if (cardId !=-1){
            val card = cardDAO.getCardById(cardId)
            println(card.value)
            card.observe(this,{card ->
                card?.let{
                    if (it != null){
                        adapter = CardAdapter(listOf(it),2)
                        recyclerView.adapter = adapter
                    }
                }
            })
        }
        else{
            println("Error")
        }



    }


}
