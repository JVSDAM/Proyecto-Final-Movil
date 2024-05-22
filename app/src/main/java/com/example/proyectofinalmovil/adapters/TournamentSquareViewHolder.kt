package com.example.proyectofinalmovil.adapters

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.proyectofinalmovil.databinding.SquareLayoutBinding
import com.example.proyectofinalmovil.models.Tournament

class TournamentSquareViewHolder (v: View): RecyclerView.ViewHolder(v) {
    private val binding = SquareLayoutBinding.bind(v)

    public fun render(tournament: Tournament, onItemClick: (Tournament) -> Unit) {
        Glide.with(binding.ivSquareImage.context).load(tournament.image).into(binding.ivSquareImage)
        binding.tvSqName.text = tournament.name

        itemView.setOnClickListener() {
            onItemClick(tournament)
        }
    }
}