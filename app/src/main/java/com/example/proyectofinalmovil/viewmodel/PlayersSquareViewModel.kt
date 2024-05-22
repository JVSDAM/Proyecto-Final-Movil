package com.example.proyectofinalmovil.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectofinalmovil.models.Player
import com.example.proyectofinalmovil.models.PlayerRes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PlayersSquareViewModel : ViewModel() {
    private var _playerList = MutableLiveData<MutableList<Player>>()
    val playerList: LiveData<MutableList<Player>> = _playerList

    fun searchTeamRoster(rosterList: PlayerRes){
        viewModelScope.launch(Dispatchers.IO) {
            var list = mutableListOf<Player>()

            for(player in rosterList.players){
                Log.d("Resultado registered", player.toString())
                list.add(player)
            }

            withContext(Dispatchers.Main) {
                _playerList.value = list
            }
        }
    }
}