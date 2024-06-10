package com.example.proyectofinalmovil.companions

import com.example.proyectofinalmovil.models.Player
import com.example.proyectofinalmovil.models.Team
import com.example.proyectofinalmovil.models.Tournament
import com.example.proyectofinalmovil.provider.ApiClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object Session {
    lateinit var sessionPlayer: Player

    var loadedPlayer: Player? = null
    var loadedTeam: Team? = null
    var loadedTournament: Tournament? = null

    var homeSettings = true

    fun update(){
        CoroutineScope(Dispatchers.IO).launch {
            val updatePlayer = ApiClient.apiClient.getPlayersById(sessionPlayer.id.toString()).body()!!
            sessionPlayer = updatePlayer
            /*val updateTeam = ApiClient.apiClient.getPlayersById(sessionPlayer.id.toString())
            sessionPlayer = updateTeam
            val updateTournament = ApiClient.apiClient.getPlayersById(sessionPlayer.id.toString())
            sessionPlayer = updateTournament*/
        }
    }

    fun changeLoadedPlayer(player: Player){
        loadedPlayer = player
    }

    fun changeLoadedTeam(team: Team){
        loadedTeam = team
    }

    fun changeLoadedTournament(tournament: Tournament){
        loadedTournament = tournament
    }
}