package com.example.yugivault.utils.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.yugivault.utils.entity.DeckWithCard
import com.example.yugivault.R
import com.example.yugivault.utils.entity.Deck

class DeckAdapter(private val onDeckClick: (DeckWithCard) -> Unit) : ListAdapter<DeckWithCard, DeckAdapter.DeckViewHolder>(DeckDiffCallback()) {
    private var selectedDecks= mutableSetOf<DeckWithCard>()

    fun getSelectedDecks(): Set<DeckWithCard> {
        return selectedDecks
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeckViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_deck, parent, false)
        return DeckViewHolder(view, onDeckClick)
    }

    override fun onBindViewHolder(holder: DeckViewHolder, position: Int) {
        val deckWithCard = getItem(position)
        holder.bind(deckWithCard, selectedDecks.contains(deckWithCard))
    }

    inner class DeckViewHolder(itemView: View, private val onDeckClick: (DeckWithCard) -> Unit) : RecyclerView.ViewHolder(itemView) {
        private val deckName: TextView = itemView.findViewById(R.id.textViewDeckName)
        private val checkBox: CheckBox = itemView.findViewById(R.id.checkBoxDeck)

/*        fun bind(deckWithCard: DeckWithCard) {
            deckName.text = deckWithCard.deck.name
            itemView.setOnClickListener {
                onDeckClick(deckWithCard)
            }
        }*/
        fun bind(deckWithCard: DeckWithCard, isSelected: Boolean) {
            deckName.text = deckWithCard.deck.name

            // Mettre à jour l'état de la case à cocher
            checkBox.isChecked = isSelected

            // Gérer les changements d'état de la case à cocher
            checkBox.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    selectedDecks.add(deckWithCard)
                } else {
                    selectedDecks.remove(deckWithCard)
                }
            }

            itemView.setOnClickListener {
                onDeckClick(deckWithCard)
            }
        }
    }

    class DeckDiffCallback : DiffUtil.ItemCallback<DeckWithCard>() {
        override fun areItemsTheSame(oldItem: DeckWithCard, newItem: DeckWithCard): Boolean {
            return oldItem.deck.deckId == newItem.deck.deckId
        }

        override fun areContentsTheSame(oldItem: DeckWithCard, newItem: DeckWithCard): Boolean {
            return oldItem == newItem
        }
    }
}
