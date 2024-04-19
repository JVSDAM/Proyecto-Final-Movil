package com.example.proyectofinalmovil.adapters

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.proyectofinalmovil.databinding.CardLayoutBinding
import com.example.proyectofinalmovil.models.Player

class PlayerViewHolder (v: View): RecyclerView.ViewHolder(v) {
    private val binding = CardLayoutBinding.bind(v)

    public fun render(player: Player, onItemClick: (Player) -> Unit){
        Glide.with(binding.ivImage.context).load(player.image).into(binding.ivImage)
        binding.tvName.text = player.name
        binding.tvInfo.text = "🖥 " + player.account

        itemView.setOnClickListener(){
            onItemClick(player)
        }
    }
}