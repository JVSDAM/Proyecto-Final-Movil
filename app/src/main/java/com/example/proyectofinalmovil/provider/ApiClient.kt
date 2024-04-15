package com.example.proyectofinalmovil.provider

import com.example.proyectofinalmovil.R
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    val retrofit2 = Retrofit.Builder()
        .baseUrl("http://172.16.160.120:3001/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val apiClient = retrofit2.create(ApiInterface::class.java)
}