package com.example.proyectofinalmovil.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectofinalmovil.R
import com.example.proyectofinalmovil.models.Tournament

class TournamentSquareAdapter (var list: List<Tournament>, val onItemClick: (Tournament)->Unit): RecyclerView.Adapter<TournamentSquareViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TournamentSquareViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.square_layout, parent, false)
        return TournamentSquareViewHolder(v)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: TournamentSquareViewHolder, position: Int) {
        holder.render(list[position], onItemClick)
    }
}