package com.example.proyectofinalmovil.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectofinalmovil.models.Tournament
import com.example.proyectofinalmovil.provider.ApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TournamentsViewModel: ViewModel() {
    private var _tournamentList = MutableLiveData<MutableList<Tournament>>()
    val tournamentList: LiveData<MutableList<Tournament>> = _tournamentList

    fun searchTournaments(par: String){
        viewModelScope.launch(Dispatchers.IO) {
            var list = mutableListOf<Tournament>()
            var results = ApiClient.apiClient.getTournamentsByName(par)
            Log.d("Resultados", results.tournaments.toString())
            for(tournament in results.tournaments){
                list.add(tournament)
                Log.d("Nombre", tournament.name)
                Log.d("Estado lista", list.toString())
            }

            withContext(Dispatchers.Main) {
                _tournamentList.value = list
            }
        }
    }
}