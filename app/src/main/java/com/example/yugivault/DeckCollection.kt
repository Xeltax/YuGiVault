package com.example.yugivault

import android.os.Bundle
import android.widget.Button
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
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.text.InputType
import android.widget.EditText
import com.example.yugivault.utils.dao.DeckDAO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
    private lateinit var buttonCreateDeck: Button
    private lateinit var deckDao: DeckDAO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_deck_collection)

        recyclerView = findViewById(R.id.recyclerViewDecks)
        deckAdapter = DeckAdapter({ deckWithCard ->
            val intent = Intent(this, DeckDetailActivity::class.java).apply {
                putExtra("deckId", deckWithCard.deck.deckId)
                putExtra("deckName", deckWithCard.deck.name)
            }
            startActivity(intent)})

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = deckAdapter

        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "dbVault"
        ).build()

        deckDao=db.deckDAO()


        buttonCreateDeck = findViewById(R.id.btnCreateDeck)
        buttonCreateDeck.setOnClickListener {
            showCreateDeckDialog()
        }





        loadDecks()


    }


    private fun showCreateDeckDialog() {
        // Crée une AlertDialog pour demander le nom du nouveau deck
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Créer un nouveau Deck")

        // Crée un champ de texte pour entrer le nom du deck
        val input = EditText(this)
        input.inputType = InputType.TYPE_CLASS_TEXT
        builder.setView(input)

        // Configure les boutons de la boîte de dialogue
        builder.setPositiveButton("Créer") { dialog, _ ->
            val deckName = input.text.toString()
            if (deckName.isNotEmpty()) {
                createNewDeck(deckName)
            }
            dialog.dismiss()
        }
        builder.setNegativeButton("Annuler") { dialog, _ ->
            dialog.cancel()
        }

        builder.show()
    }

    private fun loadDecks() {
        CoroutineScope(Dispatchers.IO).launch {
            val decks = deckDao.getALLDecksWithCards()
            withContext(Dispatchers.Main) {
                deckAdapter.submitList(decks)
            }
        }
    }

    private fun createNewDeck(name: String) {
        val newDeck = Deck(deckId = generateNewDeckId(), name = name)

        CoroutineScope(Dispatchers.IO).launch {
            deckDao.insert(newDeck)
            val updatedDecks = deckDao.getALLDecksWithCards()
            withContext(Dispatchers.Main) {
                deckAdapter.submitList(updatedDecks)
            }
        }
    }

    private fun generateNewDeckId(): Int {
        // Génère un nouvel identifiant pour le deck (logique simplifiée)
        return (deckAdapter.currentList.maxOfOrNull { it.deck.deckId } ?: 0) + 1
    }

}