package com.example.proyectofinalmovil.provider

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    val retrofit2 = Retrofit.Builder()
        .baseUrl("http://192.168.0.15:3001/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val apiClient = retrofit2.create(ApiInterface::class.java)
}