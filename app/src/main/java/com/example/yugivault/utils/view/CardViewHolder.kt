package com.example.yugivault.utils.view

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.yugivault.R
import com.example.yugivault.utils.entity.Card

class CardViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {

    val cardName : TextView = itemView.findViewById(R.id.Name)
    val cardTypeRace : TextView = itemView.findViewById(R.id.TypeRace)
    val cardAtkDef : TextView = itemView.findViewById(R.id.AtkDef)
    val cardAttribute : TextView = itemView.findViewById(R.id.Attribute)

    fun bind(card : Card) {
        cardName.text = card.name
        cardTypeRace.text = "${card.type}/${card.race}"
        cardAtkDef.text = "ATK: ${card.atk} / DEF: ${card.def}"
        cardAttribute.text = card.attribute
    }
}