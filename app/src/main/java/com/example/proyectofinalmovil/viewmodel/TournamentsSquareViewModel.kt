package com.example.proyectofinalmovil.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectofinalmovil.models.InscriptionRes
import com.example.proyectofinalmovil.models.Tournament
import com.example.proyectofinalmovil.provider.ApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TournamentsSquareViewModel : ViewModel() {
    private var _tournamentList = MutableLiveData<MutableList<Tournament>>()
    val tournamentList: LiveData<MutableList<Tournament>> = _tournamentList

    fun searchRegisteredTournaments(inscriptionList: InscriptionRes){
        viewModelScope.launch(Dispatchers.IO) {
            var list = mutableListOf<Tournament>()

            for(inscription in inscriptionList.inscriptions){
                Log.d("Inscripcion que se rec", inscription.toString())
                var tournamentResult = ApiClient.apiClient.getTournamentsById(inscription.tournamentId)
                Log.d("Resultado registered", tournamentResult.toString())
                list.add(tournamentResult)
            }

            withContext(Dispatchers.Main) {
                _tournamentList.value = list
            }
        }
    }
}