package com.example.yugivault

import android.app.AlertDialog
import androidx.activity.ComponentActivity
import android.os.Bundle
import android.text.InputType
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.yugivault.utils.dao.CardDAO
import com.example.yugivault.utils.dao.DeckDAO
import com.example.yugivault.utils.db.AppDatabase
import com.example.yugivault.utils.entity.Card
import com.example.yugivault.utils.entity.DeckCardCrossRef
import com.example.yugivault.utils.view.CardAdapter
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DeckDetailActivity : ComponentActivity(){

    private lateinit var recyclerView: RecyclerView
    private lateinit var cardAdapter: CardAdapter
    private lateinit var buttonAddCard: Button
    private lateinit var deckNameTextView: TextView
    private lateinit var deckDao: DeckDAO
    private lateinit var cardDao : CardDAO
    private var deckId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_deck)

        cardAdapter = CardAdapter(listOf(), 2, this)

        deckId = intent.getIntExtra("deckId", 0)
        val deckName = intent.getStringExtra("deckName")

        // Initialise la base de données et le DAO
        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "dbVault"
        ).build()
        deckDao = db.deckDAO()
        cardDao = db.cardDAO()

        // Initialise les vues
        recyclerView = findViewById(R.id.recyclerViewCards)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = cardAdapter

        deckNameTextView = findViewById(R.id.deckName)
        deckNameTextView.text = deckName

        buttonAddCard = findViewById(R.id.buttonAddCard)
        buttonAddCard.setOnClickListener {
            showAddCardDialog()
        }

        // Charger les cartes du deck
        loadCards()
    }

    private fun loadCards() {
        CoroutineScope(Dispatchers.IO).launch {
            val deckWithCards = deckDao.getDeckWithCards(deckId)
            withContext(Dispatchers.Main) {
                val cardAdapter = CardAdapter(deckWithCards.cards, 2, this@DeckDetailActivity)
                recyclerView.adapter = cardAdapter
            }
        }
    }

    private fun showAddCardDialog() {
        // Lancer une coroutine dans le thread Dispatchers.IO pour récupérer les noms de cartes
        CoroutineScope(Dispatchers.IO).launch {
            // Récupérer les noms et les IDs de toutes les cartes de manière asynchrone
            val cardNamesWithIds = cardDao.getAllCardNameWithId()

            // Tableau de boolean pour stocker l'état de coche de chaque carte
            val checkedItems = BooleanArray(cardNamesWithIds.size)

            // Créer un tableau de noms de cartes à afficher dans la liste de dialogues
            val cardNamesArray = arrayOfNulls<String>(cardNamesWithIds.size)
            val cardIdsArray = IntArray(cardNamesWithIds.size)
            for ((index, cardNameWithId) in cardNamesWithIds.withIndex()) {
                cardNamesArray[index] = cardNameWithId.name
                cardIdsArray[index] = cardNameWithId.uid
            }

            // Passer à l'interface utilisateur sur le thread principal pour afficher la boîte de dialogue
            withContext(Dispatchers.Main) {
                // Créer un dialogue d'alerte avec une liste de cartes à cocher
                val builder = AlertDialog.Builder(this@DeckDetailActivity)
                builder.setTitle("Ajouter des cartes")
                    .setMultiChoiceItems(cardNamesArray, checkedItems) { _, which, isChecked ->
                        // Mettre à jour l'état de coche lorsqu'un élément est coché ou décoché
                        checkedItems[which] = isChecked
                    }
                    .setPositiveButton("Ajouter") { dialog, _ ->
                        // Ajouter les cartes cochées au deck
                        for (i in checkedItems.indices) {
                            if (checkedItems[i]) {
                                // Lancer une nouvelle coroutine pour ajouter chaque carte au deck
                                println("Cartre coché : $i")
                                CoroutineScope(Dispatchers.IO).launch {
                                    addNewCard(cardIdsArray[i]) // Ajouter la carte à votre deck en passant l'ID
                                }
                            }
                        }
                        dialog.dismiss()
                        loadCards()
                    }
                    .setNegativeButton("Annuler") { dialog, _ ->
                        dialog.cancel()
                    }

                builder.create().show()
            }
        }
    }


    private fun addNewCard(cardId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            println("Deck ID : $deckId")
            println("Card ID : $cardId")
            val card = cardDao.getCardById(cardId)
            println("Card : $card")
            val deckCardCrossRef = DeckCardCrossRef(deckId, cardId)
            deckDao.insertDeckCardCrossRef(deckCardCrossRef)
        }
    }





}