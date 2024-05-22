package com.example.proyectofinalmovil.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class TeamRes(
    @SerializedName("results") val teams: List<Team>
): Serializable

data class Team(
    @SerializedName("_id") var id: String?,
    @SerializedName("image") var image: String?,
    @SerializedName("name") var name: String,
    @SerializedName("description") var description: String?,
    @SerializedName("admin_id") var adminId: String
): Serializable
