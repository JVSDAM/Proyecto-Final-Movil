package com.example.proyectofinalmovil.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class InviteRes(
    @SerializedName("results") val invites: List<Invite>
): Serializable

data class Invite(
    @SerializedName("_id") var id: String?,
    @SerializedName("team_id") var teamId: String,
    @SerializedName("player_id") var playerId: String
): Serializable