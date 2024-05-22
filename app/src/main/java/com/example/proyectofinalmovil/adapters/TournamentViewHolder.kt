package com.example.proyectofinalmovil.adapters

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.proyectofinalmovil.R
import com.example.proyectofinalmovil.databinding.CardLayoutBinding
import com.example.proyectofinalmovil.models.Tournament

class TournamentViewHolder(v: View): RecyclerView.ViewHolder(v) {
    private val binding = CardLayoutBinding.bind(v)

    public fun render(tournament: Tournament, onItemClick: (Tournament) -> Unit){
        Glide.with(binding.ivImage.context).load(tournament.image).into(binding.ivImage)
        binding.tvName.text = tournament.name
        binding.tvInfo.text = "📜 " + tournament.description
        if(tournament.description == "" || tournament.description == null){
            binding.tvInfo.text = itemView.context.getText(R.string.errorNoDescription)
        }

        itemView.setOnClickListener(){
            onItemClick(tournament)
        }
    }
}