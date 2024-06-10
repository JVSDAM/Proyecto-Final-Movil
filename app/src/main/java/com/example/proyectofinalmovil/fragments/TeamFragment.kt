package com.example.proyectofinalmovil.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.proyectofinalmovil.MainMenuActivity
import com.example.proyectofinalmovil.R
import com.example.proyectofinalmovil.adapters.PlayerSquareAdapter
import com.example.proyectofinalmovil.adapters.TournamentSquareAdapter
import com.example.proyectofinalmovil.companions.Session
import com.example.proyectofinalmovil.databinding.FragmentTeamBinding
import com.example.proyectofinalmovil.models.Invite
import com.example.proyectofinalmovil.models.Player
import com.example.proyectofinalmovil.models.Team
import com.example.proyectofinalmovil.models.Tournament
import com.example.proyectofinalmovil.provider.ApiClient
import com.example.proyectofinalmovil.viewmodel.MainMenuViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TeamFragment : Fragment() {
    private lateinit var binding: FragmentTeamBinding
    private lateinit var loadedTeam: Team
    private lateinit var admin: Player

    private var registered = false

    private var nameTOk = true
    private var descTOk = true

    private var loadedInvite: Invite? = null

    private val mainMenuVM: MainMenuViewModel by viewModels()

    val playerSquareAdapter = PlayerSquareAdapter(mutableListOf(), { player -> showPlayerInformation(player) })

    val tournamentSquareAdapter = TournamentSquareAdapter(mutableListOf(), { tournament -> showTournamentInformation(tournament) })

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreate(savedInstanceState)
        binding = FragmentTeamBinding.inflate(layoutInflater)

        loadTeam()
        setRecyclers()

        mainMenuVM.playerList.observe(this.viewLifecycleOwner) {
            playerSquareAdapter.list = it
            playerSquareAdapter.notifyDataSetChanged()
        }

        mainMenuVM.tournamentList.observe(this.viewLifecycleOwner) {
            tournamentSquareAdapter.list = it
            tournamentSquareAdapter.notifyDataSetChanged()
        }

        fillInterface()
        setListeners()

        return binding.root
    }

    private fun loadTeam(){
        loadedTeam = Session.loadedTeam!!

        if(loadedTeam.adminId == Session.sessionPlayer.id){
            //Show admin
            binding.btnTEdit.visibility = View.VISIBLE
        }else{
            //Hide admin
            binding.btnTEdit.visibility = View.GONE
        }

        if(Session.sessionPlayer.teamId == loadedTeam.id){
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
            var rosterList = ApiClient.apiClient.getPlayersByTeamId(loadedTeam.id.toString()).body()!!
            mainMenuVM.searchTeamRoster(rosterList)

            var rosterText = ""
            for(player in rosterList.players){
                if(player.staff == false && player.account != null && player.account != ""){
                    rosterText = rosterText + player.account + ", "
                }
            }
            if(rosterText != ""){
                binding.tvTRoster.setText(rosterText.dropLast(2))
            }else{
                binding.tvTRoster.setText(getText(R.string.errorEmptyField))
            }
        }

        CoroutineScope(Dispatchers.IO).launch {
            var inscriptionList = ApiClient.apiClient.getInscriptionsByTeamId(loadedTeam.id.toString()).body()!!
            mainMenuVM.searchRegisteredTournaments(inscriptionList)
        }

        binding.cardView5.visibility = View.GONE
        CoroutineScope(Dispatchers.Main).launch {
            admin = ApiClient.apiClient.getPlayersById(loadedTeam.adminId).body()!!
            Glide.with(binding.ivTAdmin).load(admin.image).into(binding.ivTAdmin)
            binding.tvTAdmin.text = admin.name
            binding.cardView5.visibility = View.VISIBLE
        }

        binding.etTEditName.setText(loadedTeam.name.toString())
        binding.etTEditDescription.setText(loadedTeam.description.toString())

        binding.btnTJoin.visibility = View.GONE

        if(registered == true){
            binding.btnTJoin.text = getText(R.string.btnLeaveTeam)
            binding.btnTJoin.visibility = View.VISIBLE
            binding.btnTJoin.icon = resources.getDrawable(R.drawable.baseline_cancel_24)
        }else{
            CoroutineScope(Dispatchers.IO).launch(){
                var results = ApiClient.apiClient.getInvitesByPlayerId(Session.sessionPlayer.id.toString()).body()!!
                for(invite in results.invites){
                    if(invite.teamId == loadedTeam.id){
                        binding.btnTJoin.text = getText(R.string.btnJoinTeam)
                        binding.btnTJoin.visibility = View.VISIBLE
                        binding.btnTJoin.icon = resources.getDrawable(R.drawable.baseline_check_circle_24)
                        loadedInvite = invite
                        break
                    }
                }
            }
        }
    }

    private fun setListeners(){
        binding.constraintTAdmin.setOnClickListener(){
            Session.changeLoadedPlayer(admin)

            (activity as MainMenuActivity).replaceFragments(PlayerFragment())
        }

        binding.btnTJoin.setOnClickListener(){
            if(registered == true){
                if(loadedTeam.adminId == Session.sessionPlayer.id.toString()){
                    deleteTeam()
                }else{
                    CoroutineScope(Dispatchers.IO).launch {
                        Session.sessionPlayer.teamId = ""
                        ApiClient.apiClient.putPlayersById(Session.sessionPlayer.id.toString(), Session.sessionPlayer)

                        Session.update()

                        Session.changeLoadedTeam(loadedTeam)

                        (activity as MainMenuActivity).replaceFragments(TeamFragment())
                    }
                }
            }else{
                CoroutineScope(Dispatchers.IO).launch {

                    Session.sessionPlayer.teamId = loadedTeam.id.toString()
                    ApiClient.apiClient.putPlayersById(Session.sessionPlayer.id.toString(), Session.sessionPlayer)

                    ApiClient.apiClient.deleteInvitesById(loadedInvite?.id.toString())

                    Session.update()

                    Session.changeLoadedTeam(loadedTeam)

                    (activity as MainMenuActivity).replaceFragments(TeamFragment())
                }
            }
        }

        binding.btnTEdit.setOnClickListener(){
            binding.clTShow.visibility = View.GONE
            binding.clTEdit.visibility = View.VISIBLE
        }

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
                loadedTeam = ApiClient.apiClient.getTeamsById(editedTeam.id.toString()).body()!!

                Session.changeLoadedTeam(loadedTeam)

                (activity as MainMenuActivity).replaceFragments(TeamFragment())
            }
        }

        binding.btnDeleteTeam.setOnClickListener(){
            deleteTeam()

        }

        binding.btnTBackEdit.setOnClickListener(){
            binding.clTShow.visibility = View.VISIBLE
            binding.clTEdit.visibility = View.GONE
        }
    }

    private fun deleteTeam(){
        var players: MutableList<Player>? = mainMenuVM.playerList.value
        if (players != null) {
            for(player in players){
                CoroutineScope(Dispatchers.IO).launch {
                    player.teamId = ""
                    ApiClient.apiClient.putPlayersById(player.id.toString(), player)
                }
            }
        }

        CoroutineScope(Dispatchers.IO).launch {
            var inscriptions = ApiClient.apiClient.getInscriptionsByTeamId(loadedTeam.id.toString()).body()!!
            for(inscription in inscriptions.inscriptions){
                ApiClient.apiClient.deleteInscriptionsById(inscription.id.toString())
            }
        }

        CoroutineScope(Dispatchers.IO).launch {
            ApiClient.apiClient.deleteTeamsById(loadedTeam.id.toString())
        }

        Session.update()
        Session.loadedTeam = null

        (activity as MainMenuActivity).replaceFragments(CreateFragment())

    }

    private fun checkBtnTConfirmEditEnabled(){
        binding.btnTConfirmEdit.isEnabled = nameTOk && descTOk
    }

    private fun setRecyclers(){
        val playerSquareLayoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.rvTRoster.layoutManager = playerSquareLayoutManager
        binding.rvTRoster.adapter = playerSquareAdapter

        val tournamentSquareLayoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.rvTTournaments.layoutManager = tournamentSquareLayoutManager
        binding.rvTTournaments.adapter = tournamentSquareAdapter
    }

    private fun showPlayerInformation(player: Player) {
        Session.changeLoadedPlayer(player)

        (activity as MainMenuActivity).replaceFragments(PlayerFragment())
    }

    private fun showTournamentInformation(tournament: Tournament) {
        Session.changeLoadedTournament(tournament)

        (activity as MainMenuActivity).replaceFragments(TournamentFragment())
    }
}