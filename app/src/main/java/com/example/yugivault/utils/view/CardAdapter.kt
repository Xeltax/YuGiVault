package com.example.yugivault.utils.view

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.yugivault.CardCollection
import com.example.yugivault.R
import com.example.yugivault.utils.entity.Card
import java.io.File

class CardAdapter(private val cards: List<Card>,type:Int,private val context :Context ): RecyclerView.Adapter<RecyclerView.ViewHolder>()    {
    private val type = type

    companion object {
        private const val TYPE_NORMAL = 1
        private const val TYPE_DETAILLED = 2
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_NORMAL -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_card, parent, false)
                CardViewHolder(view)
            }
            TYPE_DETAILLED -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_detailled_card, parent, false)
                DetailledCardViewHolder(view)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val card = cards[position]

        when (holder.itemViewType) {
            TYPE_NORMAL->{
                val cardViewHolder = holder as CardViewHolder
                cardViewHolder.bind(card)
                val imageFile = File(context.getExternalFilesDir(null),"${card.uid}.jpg")
                if(imageFile.exists()){
                    Glide.with(context)
                        .load(imageFile)
                        .into(holder.Artwork)
                }else{

                }
                cardViewHolder.itemView.setOnClickListener {
                    println("CARTE = "+card)
                    (holder.itemView.context as CardCollection).startCardDetailActivity(card)
                }
            }
            TYPE_DETAILLED-> {
                val detailledCardViewHolder = holder as DetailledCardViewHolder
                detailledCardViewHolder.bind(card)
                val imageFile = File(context.getExternalFilesDir(null),"${card.uid}.jpg")
                if(imageFile.exists()){
                    Glide.with(context)
                        .load(imageFile)
                        .into(holder.Artwork)
                }else{

                }
                detailledCardViewHolder.itemView.setOnClickListener {

                }
            }
        }
    }

    override fun getItemCount(): Int {
        return cards.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (type == 1) {
            TYPE_NORMAL
        } else {
            TYPE_DETAILLED
        }
    }
}
