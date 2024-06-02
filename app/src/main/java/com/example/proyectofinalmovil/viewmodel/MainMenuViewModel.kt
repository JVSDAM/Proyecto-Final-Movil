package com.example.proyectofinalmovil.viewmodel

import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectofinalmovil.R
import com.example.proyectofinalmovil.models.InscriptionRes
import com.example.proyectofinalmovil.models.InviteRes
import com.example.proyectofinalmovil.models.Player
import com.example.proyectofinalmovil.models.PlayerRes
import com.example.proyectofinalmovil.models.Team
import com.example.proyectofinalmovil.models.Tournament
import com.example.proyectofinalmovil.provider.ApiClient
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainMenuViewModel: ViewModel() {
    private var _playerList = MutableLiveData<MutableList<Player>>()
    val playerList: LiveData<MutableList<Player>> = _playerList

    private var _teamList = MutableLiveData<MutableList<Team>>()
    val teamList: LiveData<MutableList<Team>> = _teamList

    private var _tournamentList = MutableLiveData<MutableList<Tournament>>()
    val tournamentList: LiveData<MutableList<Tournament>> = _tournamentList

    fun searchPlayers(par: String){
        viewModelScope.launch(Dispatchers.IO) {
            var list = mutableListOf<Player>()

            var results = ApiClient.apiClient.getPlayersByName(par)

            if(results.isSuccessful){
                Log.d("Resultados", results.body()?.players.toString())
                for(player in results.body()?.players!!){
                    list.add(player)
                    Log.d("Nombre", player.name)
                    Log.d("Estado lista", list.toString())
                }

                withContext(Dispatchers.Main) {
                    _playerList.value = list
                }
            }else{
                Log.d(results.code().toString(), results.message())
            }
        }
    }

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

    fun searchInvites(inviteList: InviteRes){
        viewModelScope.launch(Dispatchers.IO) {
            var list = mutableListOf<Team>()

            for(invite in inviteList.invites){
                var teamResult = ApiClient.apiClient.getTeamsById(invite.teamId)
                list.add(teamResult)
            }

            withContext(Dispatchers.Main) {
                _teamList.value = list
            }
        }
    }
}