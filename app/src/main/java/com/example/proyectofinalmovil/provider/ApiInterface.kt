package com.example.proyectofinalmovil.provider

import com.example.proyectofinalmovil.models.Player
import com.example.proyectofinalmovil.models.PlayerRes
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiInterface {

    @GET("/players/email/{email}")
    suspend fun getPlayerByEmail(@Path("email") email: String): PlayerRes

    @GET("/players/{name}")
    suspend fun getPlayersByName(@Path("name") name: String): PlayerRes

    @GET("/players/{id}")
    suspend fun getPlayersById(@Path("id") id: String): PlayerRes

    @POST("/players")
    suspend fun postPlayers(@Body player: Player): Response<Player>



}