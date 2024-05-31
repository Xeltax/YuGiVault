package com.example.yugivault.utils.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.yugivault.utils.entity.DeckWithCard
import com.example.yugivault.R

class DeckAdapter : RecyclerView.Adapter<DeckAdapter.DeckViewHolder>() {
    private var decks: List<DeckWithCard> = listOf()

    // Méthode pour soumettre une nouvelle liste de decks
    fun submitList(newDecks: List<DeckWithCard>) {
        decks = newDecks
        notifyDataSetChanged()
    }

    // Crée un ViewHolder pour chaque élément de la liste
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeckViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_deck, parent, false)
        return DeckViewHolder(itemView)
    }

    // Associe les données aux éléments de la liste
    override fun onBindViewHolder(holder: DeckViewHolder, position: Int) {
        val currentDeck = decks[position]
        holder.bind(currentDeck)
    }

    // Retourne le nombre total d'éléments dans la liste
    override fun getItemCount(): Int {
        return decks.size
    }

    // ViewHolder pour un élément de la liste
    class DeckViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val deckNameTextView: TextView = itemView.findViewById(R.id.textViewDeckName)
        private val cardCountTextView: TextView = itemView.findViewById(R.id.textViewCardCount)

        // Associe les données au ViewHolder
        fun bind(deckWithCards: DeckWithCard) {
            deckNameTextView.text = deckWithCards.deck.name
            cardCountTextView.text = "Nombre de cartes : ${deckWithCards.cards.size}"
        }
    }
}