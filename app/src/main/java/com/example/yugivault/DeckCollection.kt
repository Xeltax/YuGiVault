package com.example.yugivault

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.yugivault.utils.db.AppDatabase
import com.example.yugivault.utils.entity.Card
import com.example.yugivault.utils.entity.Deck
import com.example.yugivault.utils.entity.DeckWithCard
import com.example.yugivault.utils.view.DeckAdapter

fun mockDecksWithCards(): List<DeckWithCard> {
    val mockDecksWithCards = mutableListOf<DeckWithCard>()

    // Création de quelques cartes fictives
    val cards = listOf(
        Card(1, "Dark Magician", "Spellcaster", "The ultimate wizard.", 2500, 2100, 7, "Dark", "Spellcaster"),
        Card(2, "Blue-Eyes White Dragon", "Dragon", "This legendary dragon is a powerful engine of destruction.", 3000, 2500, 8, "Light", "Dragon"),
        Card(3, "Red-Eyes Black Dragon", "Dragon", "A ferocious dragon with a deadly attack.", 2400, 2000, 7, "Dark", "Dragon")
        // Ajoutez autant de cartes que nécessaire
    )

    // Création de quelques decks fictifs avec des cartes associées
    val decks = listOf(
        Deck(1, "Deck 1"),
        Deck(2, "Deck 2")
        // Ajoutez autant de decks que nécessaire
    )

    // Attribution de certaines cartes à chaque deck
    decks.forEach { deck ->
        val deckCards = mutableListOf<Card>()
        // Ajoutez quelques cartes au hasard à chaque deck
        repeat((1..3).random()) {
            deckCards.add(cards.random())
        }
        mockDecksWithCards.add(DeckWithCard(deck, deckCards))
    }

    return mockDecksWithCards
}

class DeckCollection : ComponentActivity(){

    private lateinit var recyclerView: RecyclerView
    private lateinit var deckAdapter: DeckAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_deck_collection)
        Toast.makeText(this, "CardDetailActivity", Toast.LENGTH_SHORT).show()

        recyclerView = findViewById(R.id.recyclerViewDecks)
        deckAdapter = DeckAdapter()

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = deckAdapter


        val mockDecksWithCards = mockDecksWithCards()

        deckAdapter.submitList(mockDecksWithCards)

        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "dbVault"
        ).build()


    }
}