package com.example.proyectofinalmovil.provider

import com.example.proyectofinalmovil.models.Player
import com.example.proyectofinalmovil.models.PlayerRes
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiInterface {
    @GET("/players/{name}")
    suspend fun getPlayersByName(@Path("name") name: String): Player

}