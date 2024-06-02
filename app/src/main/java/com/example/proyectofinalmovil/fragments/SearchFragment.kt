package com.example.proyectofinalmovil.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.proyectofinalmovil.MainMenuActivity
import com.example.proyectofinalmovil.adapters.PlayerAdapter
import com.example.proyectofinalmovil.adapters.TeamAdapter
import com.example.proyectofinalmovil.adapters.TournamentAdapter
import com.example.proyectofinalmovil.companions.Session
import com.example.proyectofinalmovil.databinding.FragmentSearchBinding
import com.example.proyectofinalmovil.models.Player
import com.example.proyectofinalmovil.models.Team
import com.example.proyectofinalmovil.models.Tournament
import com.example.proyectofinalmovil.viewmodel.MainMenuViewModel
import com.google.android.material.tabs.TabLayout


class SearchFragment : Fragment() {
    private lateinit var binding: FragmentSearchBinding

    private val mainMenuVM: MainMenuViewModel by viewModels()

    val playerAdapter = PlayerAdapter(mutableListOf(), { player -> showPlayerInformation(player) })

    val teamAdapter = TeamAdapter(mutableListOf(), { team -> showTeamInformation(team) })

    val tournamentAdapter = TournamentAdapter(mutableListOf(), { tournament -> showTournamentInformation(tournament) })


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreate(savedInstanceState)
        binding = FragmentSearchBinding.inflate(layoutInflater)

        setRecyclers()
        setListeners()

        mainMenuVM.playerList.observe(this.viewLifecycleOwner) {
            playerAdapter.list = it
            playerAdapter.notifyDataSetChanged()
        }

        mainMenuVM.teamList.observe(this.viewLifecycleOwner) {
            teamAdapter.list = it
            teamAdapter.notifyDataSetChanged()
        }

        mainMenuVM.tournamentList.observe(this.viewLifecycleOwner) {
            tournamentAdapter.list = it
            tournamentAdapter.notifyDataSetChanged()
        }

        changeRecycler(1)
        mainMenuVM.searchPlayers("")
        mainMenuVM.searchTeams("")
        mainMenuVM.searchTournaments("")

        return binding.root
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
        val playerLayoutManager = LinearLayoutManager(context)
        binding.playerRecycler.layoutManager = playerLayoutManager
        binding.playerRecycler.adapter = playerAdapter

        val teamLayoutManager = LinearLayoutManager(context)
        binding.teamsRecycler.layoutManager = teamLayoutManager
        binding.teamsRecycler.adapter = teamAdapter

        val tournamentLayoutManager = LinearLayoutManager(context)
        binding.tournamentsRecycler.layoutManager = tournamentLayoutManager
        binding.tournamentsRecycler.adapter = tournamentAdapter
    }

    private fun setListeners() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                mainMenuVM.searchPlayers(
                    binding.searchView.query.toString().lowercase().replace(" ", "_")
                )
                mainMenuVM.searchTeams(
                    binding.searchView.query.toString().lowercase().replace(" ", "_")
                )
                mainMenuVM.searchTournaments(
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
    }

    private fun showPlayerInformation(player: Player) {
        Session.changeLoadedPlayer(player)

        (activity as MainMenuActivity).replaceFragments(PlayerFragment())

    }

    private fun showTeamInformation(team: Team) {
        Session.changeLoadedTeam(team)

        (activity as MainMenuActivity).replaceFragments(TeamFragment())
    }

    private fun showTournamentInformation(tournament: Tournament) {
        Session.changeLoadedTournament(tournament)

        (activity as MainMenuActivity).replaceFragments(TournamentFragment())
    }

}