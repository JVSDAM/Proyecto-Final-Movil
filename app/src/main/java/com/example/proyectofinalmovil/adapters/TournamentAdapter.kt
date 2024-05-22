package com.example.proyectofinalmovil.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectofinalmovil.R
import com.example.proyectofinalmovil.models.Tournament

class TournamentAdapter(var list: List<Tournament>, val onItemClick: (Tournament)->Unit): RecyclerView.Adapter<TournamentViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TournamentViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.card_layout, parent, false)
        return TournamentViewHolder(v)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: TournamentViewHolder, position: Int) {
        holder.render(list[position], onItemClick)
    }
}