package com.example.yugivault

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
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

        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "dbVault"
        ).build()

        val cardDAO = db.cardDAO()

        val intent = intent

        val cardId = intent.getIntExtra("cardId", -1)

        if(cardId !=-1){
            val card = cardDAO.getCardById(cardId)

            card.value?.let {
                adapter = CardAdapter(listOf(it),2)
                recyclerView.adapter = adapter
            }
        }
    }

}