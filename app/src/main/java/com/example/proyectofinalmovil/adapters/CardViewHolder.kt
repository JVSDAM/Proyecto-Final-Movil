package com.example.proyectofinalmovil.adapters

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectofinalmovil.databinding.CardLayoutBinding
import com.example.proyectofinalmovil.models.CardPOJO

class CardViewHolder (v: View): RecyclerView.ViewHolder(v) {
    private val binding = CardLayoutBinding.bind(v)

    public fun render(card: CardPOJO, onItemClick: (CardPOJO) -> Unit){
        //Glide.with(binding.ivImagen.context).load(articulo.image).into(binding.ivImagen)
        binding.tvName.text = card.name
        binding.tvDescription.text = "⏩" + card.description

        itemView.setOnClickListener(){
            onItemClick(card)
        }
    }
}