package com.example.proyectofinalmovil.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectofinalmovil.R
import com.example.proyectofinalmovil.models.Team

class TeamInviteAdapter (var list: List<Team>, val onItemClick: (Team)->Unit): RecyclerView.Adapter<TeamInviteViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TeamInviteViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.card_layout, parent, false)
        return TeamInviteViewHolder(v)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: TeamInviteViewHolder, position: Int) {
        holder.render(list[position], onItemClick)
    }
}