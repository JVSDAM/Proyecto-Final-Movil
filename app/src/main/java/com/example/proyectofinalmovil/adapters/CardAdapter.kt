package com.example.proyectofinalmovil.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectofinalmovil.R
import com.example.proyectofinalmovil.models.CardPOJO

class CardAdapter (var lista: List<CardPOJO>, val onItemClick: (CardPOJO)->Unit): RecyclerView.Adapter<CardViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.card_layout, parent, false)
        return CardViewHolder(v)
    }

    override fun getItemCount(): Int {
        return lista.size
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        holder.render(lista[position], onItemClick)
    }
}