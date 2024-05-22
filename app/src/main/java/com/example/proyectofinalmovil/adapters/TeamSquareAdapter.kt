package com.example.proyectofinalmovil.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectofinalmovil.R
import com.example.proyectofinalmovil.models.Team

class TeamSquareAdapter (var list: List<Team>, val onItemClick: (Team)->Unit): RecyclerView.Adapter<TeamSquareViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TeamSquareViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.square_layout, parent, false)
        return TeamSquareViewHolder(v)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: TeamSquareViewHolder, position: Int) {
        holder.render(list[position], onItemClick)
    }
}