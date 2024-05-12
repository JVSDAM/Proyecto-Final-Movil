package com.example.proyectofinalmovil.companions

import com.example.proyectofinalmovil.models.Player
import com.example.proyectofinalmovil.provider.ApiClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object Session {
    lateinit var player: Player

    fun update(){
        CoroutineScope(Dispatchers.IO).launch {
            val update = ApiClient.apiClient.getPlayersById(player.id.toString())
            player = update
        }
    }

    //lateinit var team
}