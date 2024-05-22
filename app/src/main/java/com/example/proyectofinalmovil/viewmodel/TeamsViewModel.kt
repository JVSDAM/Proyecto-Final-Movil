package com.example.proyectofinalmovil.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectofinalmovil.models.Team
import com.example.proyectofinalmovil.provider.ApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TeamsViewModel: ViewModel() {
    private var _teamList = MutableLiveData<MutableList<Team>>()
    val teamList: LiveData<MutableList<Team>> = _teamList

    fun searchTeams(par: String){
        viewModelScope.launch(Dispatchers.IO) {
            var list = mutableListOf<Team>()
            var results = ApiClient.apiClient.getTeamsByName(par)
            Log.d("Resultados", results.teams.toString())
            for(team in results.teams){
                list.add(team)
                Log.d("Nombre", team.name)
                Log.d("Estado lista", list.toString())
            }

            withContext(Dispatchers.Main) {
                _teamList.value = list
            }
        }
    }
}