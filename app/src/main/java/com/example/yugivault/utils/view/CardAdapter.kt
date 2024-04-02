package com.example.yugivault.utils.view

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.yugivault.CardCollection
import com.example.yugivault.DetailledCardActivity
import com.example.yugivault.R
import com.example.yugivault.utils.entity.Card



//cardLayout == 1 -> CardViewHolder (item_card.xml)
//cardLayout == 2 -> DetailledCardViewHolder (item_detailled_card.xml)
class CardAdapter(private val cards: List<Card>,private val cardLayout: Int) : RecyclerView.Adapter<RecyclerView.ViewHolder>()    {

    companion object {
        private const val TYPE_CARD = 1
        private const val TYPE_DETAILLED_CARD = 2
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            TYPE_CARD -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_card, parent, false)
                CardViewHolder(view)
            }
            TYPE_DETAILLED_CARD -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_detailled_card, parent, false)
                DetailledCardViewHolder(view)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val card = cards[position]

        when (holder.itemViewType) {
            TYPE_CARD -> {
                val cardViewHolder = holder as CardViewHolder
                cardViewHolder.bind(card)
                cardViewHolder.itemView.setOnClickListener {
                    (holder.itemView.context as CardCollection).startDetailledCardActivity(card)
                }
            }
            TYPE_DETAILLED_CARD -> {
                val detailledCardViewHolder = holder as DetailledCardViewHolder
                detailledCardViewHolder.bind(card)
                detailledCardViewHolder.itemView.setOnClickListener {

                }
            }
        }
    }

    override fun getItemCount(): Int {
        return cards.size
    }

    override fun getItemViewType(position: Int): Int {
        val card = cards[position]
        return if (cardLayout==1) {
            TYPE_CARD
        } else  {
            TYPE_DETAILLED_CARD
        }
    }

}
