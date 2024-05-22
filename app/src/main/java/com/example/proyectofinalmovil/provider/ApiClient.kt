package com.example.proyectofinalmovil.provider

import com.example.proyectofinalmovil.models.Inscription
import com.example.proyectofinalmovil.models.Player
import com.example.proyectofinalmovil.models.Team
import com.example.proyectofinalmovil.models.Tournament
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    val retrofit2 = Retrofit.Builder()
        .baseUrl("http://172.16.160.61:3001/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val apiClient = retrofit2.create(ApiInterface::class.java)

    suspend fun postPlayers(player: Player): Player? {
        val response = apiClient.postPlayers(player)
        return if (response.isSuccessful) {
            response.body()
        } else {
            null
        }
    }

    suspend fun postTeams(team: Team): Team?{
        val response = apiClient.postTeams(team)
        return if (response.isSuccessful) {
            response.body()
        } else {
            null
        }
    }

    suspend fun postTournaments(tournament: Tournament): Tournament?{
        val response = apiClient.postTournaments(tournament)
        return if (response.isSuccessful) {
            response.body()
        } else {
            null
        }
    }

    suspend fun postInscriptions(inscription: Inscription): Inscription?{
        val response = apiClient.postInscriptions(inscription)
        return if (response.isSuccessful) {
            response.body()
        } else {
            null
        }
    }
}