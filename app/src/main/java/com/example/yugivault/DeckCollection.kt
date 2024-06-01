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

        val buttonDeleteDeck = findViewById<Button>(R.id.btnDeleteDeck)
        buttonDeleteDeck.setOnClickListener {
            val selectedDecks = deckAdapter.getSelectedDecks()
            if (selectedDecks.isNotEmpty()) {
                showDeleteConfirmationDialog(selectedDecks)
            } else {
                Toast.makeText(this, "Aucun deck sélectionné", Toast.LENGTH_SHORT).show()
            }
        }



        loadDecks()
    }

    private fun showDeleteConfirmationDialog(decks: Set<DeckWithCard>) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Supprimer les decks")
            .setMessage("Êtes-vous sûr de vouloir supprimer ces decks?")
            .setPositiveButton("Oui") { _, _ ->
                deleteDecks(decks)
            }
            .setNegativeButton("Non") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun deleteDecks(decks: Set<DeckWithCard>) {
        // Supprimer les associations Deck-Card de la table DeckCardCrossRef
        val deckIdsToDelete = decks.map { it.deck.deckId }
        CoroutineScope(Dispatchers.IO).launch {
            deckDao.deleteDeckWithCArd(deckIdsToDelete)
            deckDao.deleteDeck(deckIdsToDelete)
            withContext(Dispatchers.Main) {
                // Recharger la liste des decks après la suppression
                loadDecks()
            }
        }
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
            withContext(Dispatchers.Main) {
                // Ajouter le nouveau deck à la liste actuelle sans recharger la liste complète
                val updatedList = mutableListOf<DeckWithCard>()
                updatedList.addAll(deckAdapter.currentList)
                updatedList.add(DeckWithCard(newDeck, emptyList()))
                deckAdapter.submitList(updatedList)
            }
        }
    }


    private fun generateNewDeckId(): Int {
        // Génère un nouvel identifiant pour le deck (logique simplifiée)
        return (deckAdapter.currentList.maxOfOrNull { it.deck.deckId } ?: 0) + 1
    }

}