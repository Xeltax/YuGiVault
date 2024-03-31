package com.example.yugivault

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


class CardCollection : ComponentActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CardAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_card_collection)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val carrdList = listOf(
            Card(1,"Dark Magician", "Spellcaster", "The ultimate wizard in terms of attack and defense.", 2500, 2100, 7, "Dark", "Normal","","","",""),
            Card(2,"Blue-Eyes White Dragon", "Dragon", "This legendary dragon is a powerful engine of destruction.", 3000, 2500, 8, "Light", "Normal","","","",""),
            Card(3,"Red-Eyes Black Dragon", "Dragon", "A ferocious dragon with a deadly attack.", 2400, 2000, 7, "Dark", "Normal","","","","")
        )

        println("Taille de la liste: ${carrdList.size}")
        adapter = CardAdapter(carrdList)
        recyclerView.adapter = adapter

/*        val Name = findViewById<TextView>(R.id.Name)
        val Attribute = findViewById<TextView>(R.id.Attribute)
        val ArtWork = findViewById<ImageView>(R.id.ArtWork)
        val TypeRace = findViewById<TextView>(R.id.TypeRace)
        val AtkDef = findViewById<TextView>(R.id.AtkDef)*/


        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "dbVault"
        ).build()

        val cardDAO = db.cardDAO()


        //GlobalScope.launch{
/*            val cards = cardDAO.getCards()
            println(cards.value.toString())
            cards.observe(this,{cards ->
                cards?.forEach(){
                    println("Card: ${it.name}, Type: ${it.type},desc: ${it.desc},id: ${it.uid}")
                    Name.text = it.name
                    Attribute.text = it.attribute
                }
            })*/
       // }

    }


}