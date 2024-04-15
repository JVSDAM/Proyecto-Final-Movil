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

class PlayersViewModel: ViewModel() {
    private var _playerList = MutableLiveData<MutableList<Player>>()
    val playerList: LiveData<MutableList<Player>> = _playerList

    /*fun searchPlayers(par: String){
        viewModelScope.launch(Dispatchers.IO) {
            var list = mutableListOf<Player>()
            if(par.length == 24){
                //lista.add(ApiClient.apiClient.getPlayersById(par))//Esto me va a devolver varios a la vez
            }else{
                list = ApiClient.apiClient.getPlayersByName(par)
                for(player in list){
                    list.add(player)
                }
                lista.add(ApiClient.apiClient.getPlayersByName(par))//Esto me va a devolver varios a la vez
            }
            withContext(Dispatchers.Main) {
                _playerList.value = list
            }
        }
    }*/
    fun searchPlayers(par: String){
        viewModelScope.launch(Dispatchers.IO) {
            val list = mutableListOf<Player>()
            list.add(ApiClient.apiClient.getPlayersByName(par))
            for(player in list){
                Log.d("Nombre", player.name)
                Log.d("Cuenta", player.account)
            }
        }
    }

}