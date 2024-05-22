package com.example.proyectofinalmovil.adapters

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.proyectofinalmovil.databinding.SquareLayoutBinding
import com.example.proyectofinalmovil.models.Player

class PlayerSquareViewHolder (v: View): RecyclerView.ViewHolder(v) {
    private val binding = SquareLayoutBinding.bind(v)

    public fun render(player: Player, onItemClick: (Player) -> Unit) {
        Glide.with(binding.ivSquareImage.context).load(player.image).into(binding.ivSquareImage)
        binding.tvSqName.text = player.name

        itemView.setOnClickListener() {
            onItemClick(player)
        }
    }
}