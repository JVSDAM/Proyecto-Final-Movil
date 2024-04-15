package com.example.proyectofinalmovil.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectofinalmovil.models.Player
import com.example.proyectofinalmovil.provider.ApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PlayersViewModel: ViewModel() {
    private var _playerList = MutableLiveData<MutableList<Player>>()
    val playerList: LiveData<MutableList<Player>> = _playerList

    fun searchPlayers(par: String){
        viewModelScope.launch(Dispatchers.IO) {
            var list = mutableListOf<Player>()
            var results = ApiClient.apiClient.getPlayersByName(par)
            Log.d("Resultados", results.players.toString())
            for(player in results.players){
                list.add(player)
                Log.d("Nombre", player.name)
                Log.d("Estado lista", list.toString())
            }

            withContext(Dispatchers.Main) {
                _playerList.value = list
            }
        }
    }
}