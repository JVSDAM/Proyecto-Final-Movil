package com.example.proyectofinalmovil.provider

import com.example.proyectofinalmovil.R
import com.example.proyectofinalmovil.models.Player
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    val retrofit2 = Retrofit.Builder()
        .baseUrl("http://172.16.160.72:3001/")
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
}