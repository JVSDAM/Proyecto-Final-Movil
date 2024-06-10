package com.example.proyectofinalmovil.fragments

import android.app.Instrumentation
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.test.platform.app.InstrumentationRegistry
import com.bumptech.glide.Glide
import com.example.proyectofinalmovil.MainMenuActivity
import com.example.proyectofinalmovil.R
import com.example.proyectofinalmovil.adapters.TeamSquareAdapter
import com.example.proyectofinalmovil.companions.Session
import com.example.proyectofinalmovil.databinding.FragmentTournamentBinding
import com.example.proyectofinalmovil.models.Inscription
import com.example.proyectofinalmovil.models.Player
import com.example.proyectofinalmovil.models.Team
import com.example.proyectofinalmovil.models.Tournament
import com.example.proyectofinalmovil.provider.ApiClient
import com.example.proyectofinalmovil.viewmodel.MainMenuViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TournamentFragment : Fragment() {
    private lateinit var binding: FragmentTournamentBinding
    private lateinit var loadedTournament: Tournament
    private lateinit var admin: Player

    private var nameToOk = true
    private var descToOk = true
    private var prizeToOk = true

    private var registered = false
    private lateinit var teamInscription: Inscription

    private val mainMenuVM: MainMenuViewModel by viewModels()

    val teamSquareAdapter = TeamSquareAdapter(mutableListOf(), { team -> showTeamInformation(team) })

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreate(savedInstanceState)
        binding = FragmentTournamentBinding.inflate(layoutInflater)

        loadTournament()
        setRecyclers()

        mainMenuVM.teamList.observe(this.viewLifecycleOwner) {
            teamSquareAdapter.list = it
            teamSquareAdapter.notifyDataSetChanged()
        }

        fillInterface()
        setListeners()

        return binding.root
    }

    private fun loadTournament(){
        loadedTournament = Session.loadedTournament!!
        if(loadedTournament.adminId == Session.sessionPlayer.id){
            //Show admin
            binding.btnToEdit.visibility = View.VISIBLE
        }else{
            //Hide admin
            binding.btnToEdit.visibility = View.GONE
        }
    }

    private fun fillInterface(){
        binding.clToShow.visibility = View.VISIBLE
        binding.clToEdit.visibility = View.GONE

        Glide.with(binding.ivToImage.context).load(loadedTournament.image).into(binding.ivToImage)

        binding.tvToName.text = loadedTournament.name

        if (loadedTournament.description != "") {
            binding.tvToDescription.text = loadedTournament.description
        } else {
            binding.tvToDescription.text = getText(R.string.errorNoDescription)
        }

        if (loadedTournament.prize != null) {
            binding.tvToPrize.text = loadedTournament.prize.toString()
        } else {
            binding.tvToPrize.text = getText(R.string.errorNoPrize)
        }

        CoroutineScope(Dispatchers.IO).launch {
            var inscriptionList = ApiClient.apiClient.getInscriptionsByTournamentId(loadedTournament.id.toString()).body()!!
            mainMenuVM.searchRegisteredTeams(inscriptionList)
        }

        binding.cvShowToAdmin.visibility = View.GONE
        CoroutineScope(Dispatchers.Main).launch {
            admin = ApiClient.apiClient.getPlayersById(loadedTournament.adminId).body()!!
            Glide.with(binding.ivToAdmin).load(admin.image).into(binding.ivToAdmin)
            binding.tvToAdmin.text = admin.name
            binding.cvShowToAdmin.visibility = View.VISIBLE
        }

        binding.btnToJoin.visibility = View.GONE
        if(Session.sessionPlayer.teamId != null){
            CoroutineScope(Dispatchers.Main).launch{
                var team = ApiClient.apiClient.getTeamsById(Session.sessionPlayer.teamId.toString()).body()!!
                if(team.adminId == Session.sessionPlayer.id){
                    binding.btnToJoin.visibility = View.VISIBLE
                    var results = ApiClient.apiClient.getInscriptionsByTeamId(Session.sessionPlayer.teamId.toString()).body()!!

                    for(inscription in results.inscriptions){
                        if(inscription.tournamentId == loadedTournament.id){
                            registered = true
                            teamInscription = inscription
                            break
                        }
                    }

                    if(registered == true){
                        binding.btnToJoin.text = getText(R.string.btnLeaveTournament)
                        binding.btnToJoin.icon = resources.getDrawable(R.drawable.baseline_cancel_24)
                    }else{
                        binding.btnToJoin.text = getText(R.string.btnJoinTournament)
                        binding.btnToJoin.icon = resources.getDrawable(R.drawable.baseline_add_circle_24)

                    }
                }
            }
        }

        binding.etToEditName.setText(loadedTournament.name.toString())
        binding.etToEditDescription.setText(loadedTournament.description.toString())
        binding.etToEditPrize.setText(loadedTournament.prize.toString())

    }

    private fun setListeners(){
        binding.cvShowToAdmin.setOnClickListener(){
            Session.changeLoadedPlayer(admin)

            (activity as MainMenuActivity).replaceFragments(PlayerFragment())
        }

        binding.btnToEdit.setOnClickListener(){
            binding.clToShow.visibility = View.GONE
            binding.clToEdit.visibility = View.VISIBLE
        }

        binding.etToEditName.addTextChangedListener {
            nameToOk = true

            if (binding.etToEditName.text.toString() == "") {
                binding.etToEditName.error = getText(R.string.errorEmptyField)
                nameToOk = false
            } else {
                if (binding.etToEditName.text.toString().length > 16) {
                    binding.etToEditName.error = getText(R.string.errorTooLong)
                    nameToOk = false
                }
            }

            checkBtnToConfirmEditEnabled()
        }

        binding.etToEditDescription.addTextChangedListener {
            descToOk = true

            if (binding.etToEditDescription.text.toString().length > 60) {
                binding.etToEditDescription.error = getText(R.string.errorTooLong)
                descToOk = false
            }

            checkBtnToConfirmEditEnabled()
        }

        binding.btnToConfirmEdit.setOnClickListener(){
            CoroutineScope(Dispatchers.IO).launch {
                var editedTournament = loadedTournament

                editedTournament.name = binding.etToEditName.text.toString()
                editedTournament.description = binding.etToEditDescription.text.toString()
                editedTournament.prize = binding.etToEditPrize.text.toString()

                ApiClient.apiClient.putTournamentsById(editedTournament.id.toString(), editedTournament)
                editedTournament = ApiClient.apiClient.getTournamentsById(editedTournament.id.toString()).body()!!

                Session.changeLoadedTournament(editedTournament)

                (activity as MainMenuActivity).replaceFragments(TournamentFragment())
            }
        }

        binding.btnToJoin.setOnClickListener(){
            if(registered == true){
                CoroutineScope(Dispatchers.IO).launch {
                    ApiClient.apiClient.deleteInscriptionsById(teamInscription.id.toString())

                    Session.changeLoadedTournament(loadedTournament)

                    (activity as MainMenuActivity).replaceFragments(TournamentFragment())
                }
                //Delete inscription, reload activity
            }else{
                CoroutineScope(Dispatchers.IO).launch {
                    ApiClient.apiClient.postInscriptions(Inscription(
                        null,
                        Session.sessionPlayer.teamId.toString(),
                        loadedTournament.id.toString()
                    ))

                    Session.changeLoadedTournament(loadedTournament)

                    (activity as MainMenuActivity).replaceFragments(TournamentFragment())
                }

            }
        }

        binding.btnDeleteTournament.setOnClickListener(){
            CoroutineScope(Dispatchers.Main).launch {
                Log.d("torneo eliminar", loadedTournament.id.toString())
                var inscriptions = ApiClient.apiClient.getInscriptionsByTournamentId(loadedTournament.id.toString()).body()!!
                for(inscription in inscriptions.inscriptions){
                    ApiClient.apiClient.deleteInscriptionsById(inscription.id.toString())
                }
            }

            CoroutineScope(Dispatchers.IO).launch {
                ApiClient.apiClient.deleteTournamentsById(loadedTournament.id.toString())
            }

            Session.update()

            (activity as MainMenuActivity).replaceFragments(CreateFragment())
        }

        binding.btnToBackEdit.setOnClickListener(){
            binding.clToShow.visibility = View.VISIBLE
            binding.clToEdit.visibility = View.GONE
        }
    }

    private fun checkBtnToConfirmEditEnabled(){
        binding.btnToConfirmEdit.isEnabled = nameToOk && descToOk && prizeToOk
    }

    private fun setRecyclers(){
        val teamsLayoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.rvToTeams.layoutManager = teamsLayoutManager
        binding.rvToTeams.adapter = teamSquareAdapter
    }

    private fun showTeamInformation(team: Team) {
        Session.changeLoadedTeam(team)

        (activity as MainMenuActivity).replaceFragments(TeamFragment())
    }
}