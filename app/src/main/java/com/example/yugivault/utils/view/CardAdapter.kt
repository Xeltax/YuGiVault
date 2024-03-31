package com.example.yugivault.utils.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.yugivault.R
import com.example.yugivault.utils.entity.Card

class CardAdapter(private val cards: List<Card>): RecyclerView.Adapter<CardViewHolder>()    {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_card, parent, false)
        return CardViewHolder(view)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        holder.bind(cards[position])
    }

    override fun getItemCount(): Int {
        return cards.size
    }
}
