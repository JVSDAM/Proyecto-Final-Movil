package com.example.proyectofinalmovil.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class InscriptionRes(
    @SerializedName("results") val inscriptions: List<Inscription>
): Serializable

data class Inscription(
    @SerializedName("_id") var id: String?,
    @SerializedName("team_id") var teamId: String,
    @SerializedName("tournament_id") var tournamentId: String
): Serializable

