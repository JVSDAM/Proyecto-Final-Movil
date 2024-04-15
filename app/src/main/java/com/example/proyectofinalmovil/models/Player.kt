package com.example.proyectofinalmovil.models
import com.google.gson.annotations.SerializedName
import java.io.Serializable
data class PlayerRes(
    @SerializedName("results") val players: List<Player>
): Serializable

data class Player(
    @SerializedName("_id") var id: String, //TODO va a ser problematico obtener la id de los items
    @SerializedName("name") var name: String,
    @SerializedName("email") var email: String,
    @SerializedName("password") var password: String,
    @SerializedName("account") var account: String,
    @SerializedName("team_id") var teamId: String
): Serializable

/*data class Oid(
    @SerializedName("oid") var oid: String
): Serializable*/
