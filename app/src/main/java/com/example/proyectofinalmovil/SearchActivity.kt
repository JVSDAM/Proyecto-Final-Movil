package com.example.proyectofinalmovil

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SearchView
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.proyectofinalmovil.adapters.PlayerAdapter
import com.example.proyectofinalmovil.adapters.TeamAdapter
import com.example.proyectofinalmovil.adapters.TournamentAdapter
import com.example.proyectofinalmovil.databinding.ActivitySearchBinding
import com.example.proyectofinalmovil.models.Player
import com.example.proyectofinalmovil.models.Team
import com.example.proyectofinalmovil.models.Tournament
import com.example.proyectofinalmovil.viewmodel.PlayersViewModel
import com.example.proyectofinalmovil.viewmodel.TeamsViewModel
import com.example.proyectofinalmovil.viewmodel.TournamentsViewModel


class SearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchBinding

    private val playersVM: PlayersViewModel by viewModels()
    val playerAdapter = PlayerAdapter(mutableListOf(), { player -> showPlayerInformation(player) })

    private val teamsVM: TeamsViewModel by viewModels()
    val teamAdapter = TeamAdapter(mutableListOf(), { team -> showTeamInformation(team) })

    private val tournamentsVM: TournamentsViewModel by viewModels()
    val tournamentAdapter = TournamentAdapter(mutableListOf(), { tournament -> showTournamentInformation(tournament) })


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setRecyclers()
        setListeners()

        playersVM.playerList.observe(this) {
            playerAdapter.list = it
            playerAdapter.notifyDataSetChanged()
        }

        teamsVM.teamList.observe(this) {
            teamAdapter.list = it
            teamAdapter.notifyDataSetChanged()
        }

        tournamentsVM.tournamentList.observe(this) {
            tournamentAdapter.list = it
            tournamentAdapter.notifyDataSetChanged()
        }

        changeRecycler(1)
        playersVM.searchPlayers("")
        teamsVM.searchTeams("")
        tournamentsVM.searchTournaments("")
    }



    private fun changeRecycler(number: Int){
        binding.btnShowPlayers.setBackgroundColor(Color.parseColor("#FFE1C53A"))
        binding.btnShowTeams.setBackgroundColor(Color.parseColor("#FFE1C53A"))
        binding.btnShowTournaments.setBackgroundColor(Color.parseColor("#FFE1C53A"))

        binding.playerRecycler.visibility = View.GONE
        binding.teamsRecycler.visibility = View.GONE
        binding.tournamentsRecycler.visibility = View.GONE

        if(number == 1){
            binding.btnShowPlayers.setBackgroundColor(Color.parseColor("#FF806F1F"))
            binding.playerRecycler.visibility = View.VISIBLE
        }
        if(number == 2){
            binding.btnShowTeams.setBackgroundColor(Color.parseColor("#FF806F1F"))
            binding.teamsRecycler.visibility = View.VISIBLE
        }
        if(number == 3){
            binding.btnShowTournaments.setBackgroundColor(Color.parseColor("#FF806F1F"))
            binding.tournamentsRecycler.visibility = View.VISIBLE
        }
    }

    private fun setRecyclers() {
        val playerLayoutManager = LinearLayoutManager(this)
        binding.playerRecycler.layoutManager = playerLayoutManager
        binding.playerRecycler.adapter = playerAdapter

        val teamLayoutManager = LinearLayoutManager(this)
        binding.teamsRecycler.layoutManager = teamLayoutManager
        binding.teamsRecycler.adapter = teamAdapter

        val tournamentLayoutManager = LinearLayoutManager(this)
        binding.tournamentsRecycler.layoutManager = tournamentLayoutManager
        binding.tournamentsRecycler.adapter = tournamentAdapter
    }

    private fun setListeners() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                playersVM.searchPlayers(
                    binding.searchView.query.toString().lowercase().replace(" ", "_")
                )
                teamsVM.searchTeams(
                    binding.searchView.query.toString().lowercase().replace(" ", "_")
                )
                tournamentsVM.searchTournaments(
                    binding.searchView.query.toString().lowercase().replace(" ", "_")
                )
                return true
            }
        })
        binding.btnShowPlayers.setOnClickListener(){
            changeRecycler(1)
        }
        binding.btnShowTeams.setOnClickListener(){
            changeRecycler(2)
        }
        binding.btnShowTournaments.setOnClickListener(){
            changeRecycler(3)
        }

        /*binding.toggleButton.addOnButtonCheckedListener { toggleButton, checkedId, isChecked ->

        }*/
    }

    private fun showPlayerInformation(player: Player) {
        Log.d("Enviando datos", "a PLAYER PROFILE de " + player.name)
        val i = Intent(this, PlayerActivity::class.java).apply {
            putExtra("PLAYER", player)
        }

        startActivity(i)
    }

    private fun showTeamInformation(team: Team) {
        Log.d("Enviando datos", "a TEAM PROFILE de " + team.name)
        val i = Intent(this, TeamActivity::class.java).apply {
            putExtra("TEAM", team)
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
        startActivity(Intent(this, PlayerActivity::class.java))
    }
}