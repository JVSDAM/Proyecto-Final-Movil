package com.example.proyectofinalmovil.adapters

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.proyectofinalmovil.R
import com.example.proyectofinalmovil.databinding.CardLayoutBinding
import com.example.proyectofinalmovil.models.Team

class TeamViewHolder (v: View): RecyclerView.ViewHolder(v) {
    private val binding = CardLayoutBinding.bind(v)

    public fun render(team: Team, onItemClick: (Team) -> Unit){
        Glide.with(binding.ivImage.context).load(team.image).into(binding.ivImage)
        binding.tvName.text = team.name
        binding.tvInfo.text = "📜 " + team.description
        if(team.description == "" || team.description == null){
            binding.tvInfo.text = itemView.context.getText(R.string.errorNoDescription)
        }

        itemView.setOnClickListener(){
            onItemClick(team)
        }
    }
}