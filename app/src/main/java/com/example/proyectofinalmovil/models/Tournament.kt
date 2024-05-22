package com.example.proyectofinalmovil.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class TournamentRes(
    @SerializedName("results") val tournaments: List<Tournament>
): Serializable

data class Tournament(
    @SerializedName("_id") var id: String?,
    @SerializedName("image") var image: String?,
    @SerializedName("name") var name: String,
    @SerializedName("description") var description: String?,
    @SerializedName("prize") var prize: String?,
    @SerializedName("admin_id") var adminId: String
): Serializable
