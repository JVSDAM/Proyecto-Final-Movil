package com.example.proyectofinalmovil

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.map
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.proyectofinalmovil.adapters.PlayerSquareAdapter
import com.example.proyectofinalmovil.adapters.TeamAdapter
import com.example.proyectofinalmovil.adapters.TeamSquareAdapter
import com.example.proyectofinalmovil.adapters.TournamentSquareAdapter
import com.example.proyectofinalmovil.companions.Session
import com.example.proyectofinalmovil.databinding.ActivityTeamBinding
import com.example.proyectofinalmovil.models.Inscription
import com.example.proyectofinalmovil.models.Player
import com.example.proyectofinalmovil.models.Team
import com.example.proyectofinalmovil.models.Tournament
import com.example.proyectofinalmovil.provider.ApiClient
import com.example.proyectofinalmovil.viewmodel.PlayersSquareViewModel
import com.example.proyectofinalmovil.viewmodel.TeamsSquareViewModel
import com.example.proyectofinalmovil.viewmodel.TeamsViewModel
import com.example.proyectofinalmovil.viewmodel.TournamentsSquareViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TeamActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTeamBinding
    private lateinit var loadedTeam: Team
    private lateinit var admin: Player

    private var registered = false

    private var nameTOk = true
    private var descTOk = true

    private val playersSquareVM: PlayersSquareViewModel by viewModels()
    val playerSquareAdapter = PlayerSquareAdapter(mutableListOf(), { player -> showPlayerInformation(player) })

    private val tournamentsSquareVM: TournamentsSquareViewModel by viewModels()
    val tournamentSquareAdapter = TournamentSquareAdapter(mutableListOf(), { tournament -> showTournamentInformation(tournament) })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityTeamBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        loadTeam()
        setRecyclers()

        playersSquareVM.playerList.observe(this) {
            playerSquareAdapter.list = it
            playerSquareAdapter.notifyDataSetChanged()
        }

        tournamentsSquareVM.tournamentList.observe(this) {
            tournamentSquareAdapter.list = it
            tournamentSquareAdapter.notifyDataSetChanged()
        }

        fillInterface()
        setListeners()
    }

    private fun loadTeam(){
        loadedTeam = intent.getSerializableExtra("TEAM") as Team
        Log.d("En equipo", Session.player.toString())
        if(loadedTeam.adminId == Session.player.id){
            //Show admin
            binding.btnTEdit.visibility = View.VISIBLE
        }else{
            //Hide admin
            binding.btnTEdit.visibility = View.GONE
        }

        if(Session.player.teamId == loadedTeam.id){
            registered = true
        }else{
            registered = false
        }

        //fillInterface()
    }

    private fun fillInterface(){
        binding.clTShow.visibility = View.VISIBLE
        binding.clTEdit.visibility = View.GONE

        Glide.with(binding.ivTImage.context).load(loadedTeam.image).into(binding.ivTImage)

        binding.tvTName.text = loadedTeam.name

        if (loadedTeam.description != "") {
            binding.tvTDescription.text = loadedTeam.description
        } else {
            binding.tvTDescription.text = getText(R.string.errorNoDescription)
        }

        CoroutineScope(Dispatchers.IO).launch {
            var rosterList = ApiClient.apiClient.getPlayersByTeamId(loadedTeam.id.toString())
            playersSquareVM.searchTeamRoster(rosterList)
        }

        CoroutineScope(Dispatchers.IO).launch {
            var inscriptionList = ApiClient.apiClient.getInscriptionsByTeamId(loadedTeam.id.toString())
            tournamentsSquareVM.searchRegisteredTournaments(inscriptionList)
        }

        CoroutineScope(Dispatchers.IO).launch {
            admin = ApiClient.apiClient.getPlayersById(loadedTeam.adminId)
            binding.tvTAdmin.text = admin.name
        }

        binding.etTEditName.setText(loadedTeam.name.toString())
        binding.etTEditDescription.setText(loadedTeam.description.toString())

        binding.btnTJoin.visibility = View.VISIBLE
        if(Session.player.teamId != "" && Session.player.teamId != loadedTeam.id){
            binding.btnTJoin.visibility = View.GONE
        }

        if(registered == true){
            binding.btnTJoin.text = "Leave Team"
        }else{
            binding.btnTJoin.text = "Join Team"
        }
    }

    private fun setListeners(){
        binding.constraintTAdmin.setOnClickListener(){
            val i = Intent(this, PlayerActivity::class.java).apply {
                putExtra("PLAYER", admin)
            }
            startActivity(i)
        }

        binding.btnTJoin.setOnClickListener(){
            if(registered == true){
                CoroutineScope(Dispatchers.IO).launch {
                    Session.player.teamId = ""
                    ApiClient.apiClient.putPlayersById(Session.player.id.toString(), Session.player)

                    startActivity(Intent(this@TeamActivity, TeamActivity::class.java).apply {
                        putExtra("TEAM", loadedTeam)
                    })
                }
                //Delete inscription, reload activity
            }else{
                CoroutineScope(Dispatchers.IO).launch {
                    Session.player.teamId = loadedTeam.id.toString()
                    ApiClient.apiClient.putPlayersById(Session.player.id.toString(), Session.player)

                    startActivity(Intent(this@TeamActivity, TeamActivity::class.java).apply {
                        putExtra("TEAM", loadedTeam)
                    })
                }

            }
        }

        binding.btnTEdit.setOnClickListener(){
            binding.clTShow.visibility = View.GONE
            binding.clTEdit.visibility = View.VISIBLE
        }

        /*binding.btnTEditImage.setOnClickListener(){
            //Todo put image. Que el señor nos tenga en su gloria
        }*/

        binding.etTEditName.addTextChangedListener {
                nameTOk = true

                if (binding.etTEditName.text.toString() == "") {
                    binding.etTEditName.error = getText(R.string.errorEmptyField)
                    nameTOk = false
                } else {
                    if (binding.etTEditName.text.toString().length > 16) {
                        binding.etTEditName.error = getText(R.string.errorTooLong)
                        nameTOk = false
                    }
                }

                checkBtnTConfirmEditEnabled()
        }

        binding.etTEditDescription.addTextChangedListener {
                descTOk = true

                if (binding.etTEditDescription.text.toString().length > 60) {
                    binding.etTEditDescription.error = getText(R.string.errorTooLong)
                    descTOk = false
                }

                checkBtnTConfirmEditEnabled()
        }

        binding.btnTConfirmEdit.setOnClickListener(){
            CoroutineScope(Dispatchers.IO).launch {
                var editedTeam = loadedTeam
                editedTeam.name = binding.etTEditName.text.toString()
                editedTeam.description = binding.etTEditDescription.text.toString()
                ApiClient.apiClient.putTeamsById(editedTeam.id.toString(), editedTeam)
                loadedTeam = ApiClient.apiClient.getTeamsById(editedTeam.id.toString())

                startActivity(Intent(this@TeamActivity,TeamActivity::class.java).apply {
                    putExtra("TEAM", loadedTeam)
                })
            }
        }

        binding.btnDeleteTeam.setOnClickListener(){

            var players: MutableList<Player>? = playersSquareVM.playerList.value
            if (players != null) {
                for(player in players){
                    CoroutineScope(Dispatchers.IO).launch {
                        player.teamId = ""
                        ApiClient.apiClient.putPlayersById(player.id.toString(), player)
                    }
                }
            }

            CoroutineScope(Dispatchers.IO).launch {
                var inscriptions = ApiClient.apiClient.getInscriptionsByTeamId(loadedTeam.id.toString())
                for(inscription in inscriptions.inscriptions){
                    ApiClient.apiClient.deleteInscriptionsById(inscription.id.toString())
                }
            }

            CoroutineScope(Dispatchers.IO).launch {
                ApiClient.apiClient.deleteTeamsById(loadedTeam.id.toString())
            }

            Session.update()
            startActivity(Intent(this, CreateActivity::class.java))
        }

        binding.btnTBackEdit.setOnClickListener(){
            binding.clTShow.visibility = View.VISIBLE
            binding.clTEdit.visibility = View.GONE
        }
    }

    private fun checkBtnTConfirmEditEnabled(){
        binding.btnTConfirmEdit.isEnabled = nameTOk && descTOk
    }

    private fun setRecyclers(){
        val playerSquareLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.rvTRoster.layoutManager = playerSquareLayoutManager
        binding.rvTRoster.adapter = playerSquareAdapter

        val tournamentSquareLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.rvTTournaments.layoutManager = tournamentSquareLayoutManager
        binding.rvTTournaments.adapter = tournamentSquareAdapter
    }

    private fun showPlayerInformation(player: Player) {
        Log.d("Enviando datos", "a PLAYER PROFILE de " + player.name)
        val i = Intent(this, PlayerActivity::class.java).apply {
            putExtra("PLAYER", player)
        }

        startActivity(i)
    }

    private fun showTournamentInformation(tournament: Tournament) {
        Log.d("Enviando datos", "a TOURNAMENT PROFILE de " + tournament.name)
        val i = Intent(this, TournamentActivity::class.java).apply {
            putExtra("TOURNAMENT", tournament)
        }

        startActivity(i)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, SearchActivity::class.java))
    }
}