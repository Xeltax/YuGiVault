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
import com.example.yugivault.utils.view.OnItemClickListener
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


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


    fun startDetailledCardActivity(card: Card) {
        val intent = Intent(this, DetailledCardActivity::class.java)
        intent.putExtra("CARD_ID", card.uid) //cle,valeur
        println("onItemClick")
        startActivity(intent)
        finish()
    }


}