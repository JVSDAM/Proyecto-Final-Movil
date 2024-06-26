package com.example.proyectofinalmovil.provider

import com.example.proyectofinalmovil.models.Inscription
import com.example.proyectofinalmovil.models.InscriptionRes
import com.example.proyectofinalmovil.models.Invite
import com.example.proyectofinalmovil.models.InviteRes
import com.example.proyectofinalmovil.models.Player
import com.example.proyectofinalmovil.models.PlayerRes
import com.example.proyectofinalmovil.models.Team
import com.example.proyectofinalmovil.models.TeamRes
import com.example.proyectofinalmovil.models.Tournament
import com.example.proyectofinalmovil.models.TournamentRes
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiInterface {
    //PLAYERS
    @POST("/players")
    suspend fun postPlayers(@Body player: Player): Response<Player>

    @GET("/players/email/{email}")
    suspend fun getPlayerByEmail(@Path("email") email: String): Response<PlayerRes>

    @GET("/players/{name}")
    suspend fun getPlayersByName(@Path("name") name: String): Response<PlayerRes>

    @GET("/players/id/{id}")
    suspend fun getPlayersById(@Path("id") id: String): Response<Player>

    @GET("/players/team/{team_id}")
    suspend fun getPlayersByTeamId(@Path("team_id") teamId: String): Response<PlayerRes>

    @PUT("/players/id/{id}")
    suspend fun putPlayersById(@Path("id") id: String, @Body player: Player): Response<Player>

    @DELETE("players/id/{id}")
    suspend fun deletePlayersById(@Path("id") id: String): Response<Player>

    //TEAMS
    @POST("/teams")
    suspend fun postTeams(@Body team: Team): Response<Team>

    @GET("/teams/{name}")
    suspend fun getTeamsByName(@Path("name") name: String): Response<TeamRes>

    @GET("/teams/id/{id}")
    suspend fun getTeamsById(@Path("id") id: String): Response<Team>

    @PUT("/teams/id/{id}")
    suspend fun putTeamsById(@Path("id") id: String, @Body team: Team): Response<Team>

    @DELETE("teams/id/{id}")
    suspend fun deleteTeamsById(@Path("id") id: String): Response<Team>

    //TOURNAMENTS
    @POST("/tournaments")
    suspend fun postTournaments(@Body tournament: Tournament): Response<Tournament>

    @GET("/tournaments/{name}")
    suspend fun getTournamentsByName(@Path("name") name: String): Response<TournamentRes>

    @GET("/tournaments/id/{id}")
    suspend fun getTournamentsById(@Path("id") id: String): Response<Tournament>

    @PUT("/tournaments/id/{id}")
    suspend fun putTournamentsById(@Path("id") id: String, @Body tournament: Tournament): Response<Tournament>

    @DELETE("tournaments/id/{id}")
    suspend fun deleteTournamentsById(@Path("id") id: String): Response<Team>

    //INSCRIPTIONS
    @POST("/inscriptions")
    suspend fun postInscriptions(@Body inscription: Inscription): Response<Inscription>

    @GET("/inscriptions/team/{team_id}")
    suspend fun getInscriptionsByTeamId(@Path("team_id") teamId: String): Response<InscriptionRes>

    @GET("/inscriptions/tournament/{tournament_id}")
    suspend fun getInscriptionsByTournamentId(@Path("tournament_id") tournamentId: String): Response<InscriptionRes>

    @DELETE("/inscriptions/id/{id}")
    suspend fun deleteInscriptionsById(@Path("id") id: String): Response<Inscription>

    //INSCRIPTIONS
    @POST("/invites")
    suspend fun postInvites(@Body invite: Invite): Response<Invite>

    @GET("/invites/player/{player_id}")
    suspend fun getInvitesByPlayerId(@Path("player_id") teamId: String): Response<InviteRes>

    @DELETE("/invites/id/{id}")
    suspend fun deleteInvitesById(@Path("id") id: String): Response<Invite>
}