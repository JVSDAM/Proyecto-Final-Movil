package com.example.proyectofinalmovil.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectofinalmovil.R
import com.example.proyectofinalmovil.models.Player

class PlayerSquareAdapter (var list: List<Player>, val onItemClick: (Player)->Unit): RecyclerView.Adapter<PlayerSquareViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerSquareViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.square_layout, parent, false)
        return PlayerSquareViewHolder(v)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: PlayerSquareViewHolder, position: Int) {
        holder.render(list[position], onItemClick)
    }
}