package com.example.proyectofinalmovil.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectofinalmovil.models.InscriptionRes
import com.example.proyectofinalmovil.models.Team
import com.example.proyectofinalmovil.provider.ApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TeamsSquareViewModel: ViewModel() {
    private var _teamList = MutableLiveData<MutableList<Team>>()
    val teamList: LiveData<MutableList<Team>> = _teamList

    fun searchRegisteredTeams(inscriptionList: InscriptionRes){
        viewModelScope.launch(Dispatchers.IO) {
            var list = mutableListOf<Team>()

            for(inscription in inscriptionList.inscriptions){
                var teamResult = ApiClient.apiClient.getTeamsById(inscription.teamId)
                Log.d("Resultado registered", teamResult.toString())
                list.add(teamResult)
            }

            withContext(Dispatchers.Main) {
                _teamList.value = list
            }
        }
    }
}