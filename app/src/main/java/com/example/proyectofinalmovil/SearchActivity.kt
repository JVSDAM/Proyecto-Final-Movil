package com.example.proyectofinalmovil

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.SearchView
import androidx.activity.OnBackPressedCallback
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
import com.google.android.material.tabs.TabLayout


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
        binding.playerRecycler.visibility = View.GONE
        binding.teamsRecycler.visibility = View.GONE
        binding.tournamentsRecycler.visibility = View.GONE

        if(number == 1){
            binding.playerRecycler.visibility = View.VISIBLE
        }
        if(number == 2){
            binding.teamsRecycler.visibility = View.VISIBLE
        }
        if(number == 3){
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

        binding.tlSearch.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener{
            override fun onTabSelected(p0: TabLayout.Tab?) {
                if(p0 == binding.tlSearch.getTabAt(0)){
                    changeRecycler(1)
                }
                if(p0 == binding.tlSearch.getTabAt(1)){
                    changeRecycler(2)
                }
                if(p0 == binding.tlSearch.getTabAt(2)){
                    changeRecycler(3)
                }
            }

            override fun onTabUnselected(p0: TabLayout.Tab?) {
                //No necesario pero requerido
            }

            override fun onTabReselected(p0: TabLayout.Tab?) {
                //No necesario pero requerido
            }

        })

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                startActivity(Intent(this@SearchActivity, PlayerActivity::class.java))
            }
        })
    }

    private fun showPlayerInformation(player: Player) {
        val i = Intent(this, PlayerActivity::class.java).apply {
            putExtra("PLAYER", player)
        }

        startActivity(i)
    }

    private fun showTeamInformation(team: Team) {
        val i = Intent(this, TeamActivity::class.java).apply {
            putExtra("TEAM", team)
        }

        startActivity(i)
    }

    private fun showTournamentInformation(tournament: Tournament) {
        val i = Intent(this, TournamentActivity::class.java).apply {
            putExtra("TOURNAMENT", tournament)
        }

        startActivity(i)
    }

}