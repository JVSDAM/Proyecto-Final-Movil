package com.example.proyectofinalmovil.models
import com.google.gson.annotations.SerializedName
import java.io.Serializable
data class PlayerRes(
    @SerializedName("results") val players: List<Player>
): Serializable

data class Player(
    @SerializedName("_id") var id: String,
    @SerializedName("image") var image: String,
    @SerializedName("name") var name: String,
    @SerializedName("staff") var staff: Boolean,
    @SerializedName("email") var email: String,
    @SerializedName("password") var password: String,
    @SerializedName("description") var description: String,
    @SerializedName("account") var account: String,
    @SerializedName("contact") var contact: String,
    @SerializedName("team_id") var teamId: String


): Serializable

/*data class Oid(
    @SerializedName("oid") var oid: String
): Serializable*/
