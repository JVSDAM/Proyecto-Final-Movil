package com.example.proyectofinalmovil.adapters

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.proyectofinalmovil.databinding.SquareLayoutBinding
import com.example.proyectofinalmovil.models.Team

class TeamSquareViewHolder (v: View): RecyclerView.ViewHolder(v) {
    private val binding = SquareLayoutBinding.bind(v)

    public fun render(team: Team, onItemClick: (Team) -> Unit) {
        Glide.with(binding.ivSquareImage.context).load(team.image).into(binding.ivSquareImage)
        binding.tvSqName.text = team.name

        itemView.setOnClickListener() {
            onItemClick(team)
        }
    }
}