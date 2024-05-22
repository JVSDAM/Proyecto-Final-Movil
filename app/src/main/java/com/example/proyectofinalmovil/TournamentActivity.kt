package com.example.proyectofinalmovil

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.proyectofinalmovil.adapters.TeamSquareAdapter
import com.example.proyectofinalmovil.companions.Session
import com.example.proyectofinalmovil.databinding.ActivityTournamentBinding
import com.example.proyectofinalmovil.models.Inscription
import com.example.proyectofinalmovil.models.Player
import com.example.proyectofinalmovil.models.Team
import com.example.proyectofinalmovil.models.Tournament
import com.example.proyectofinalmovil.provider.ApiClient
import com.example.proyectofinalmovil.viewmodel.TeamsSquareViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TournamentActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTournamentBinding
    private lateinit var loadedTournament: Tournament
    private lateinit var admin: Player

    private var nameToOk = true
    private var descToOk = true
    private var prizeToOk = true

    private var registered = false
    private lateinit var teamInscription: Inscription

    private val teamsSquareVM: TeamsSquareViewModel by viewModels()
    val teamSquareAdapter = TeamSquareAdapter(mutableListOf(), { team -> showTeamInformation(team) })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityTournamentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        loadTournament()
        setRecyclers()

        teamsSquareVM.teamList.observe(this) {
            teamSquareAdapter.list = it
            teamSquareAdapter.notifyDataSetChanged()
        }

        fillInterface()
        setListeners()
    }

    private fun loadTournament(){
        loadedTournament = intent.getSerializableExtra("TOURNAMENT") as Tournament
        Log.d("En torneo", Session.player.toString())
        if(loadedTournament.adminId == Session.player.id){
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
            var inscriptionList = ApiClient.apiClient.getInscriptionsByTournamentId(loadedTournament.id.toString())
            teamsSquareVM.searchRegisteredTeams(inscriptionList)
        }

        CoroutineScope(Dispatchers.IO).launch {
            admin = ApiClient.apiClient.getPlayersById(loadedTournament.adminId)
            binding.tvToAdmin.text = admin.name
        }

        binding.btnToJoin.visibility = View.GONE
        if(Session.player.teamId != null){
            CoroutineScope(Dispatchers.IO).launch{
                var team = ApiClient.apiClient.getTeamsById(Session.player.teamId.toString())
                if(team.adminId == Session.player.id){
                    binding.btnToJoin.visibility = View.VISIBLE
                    var results = ApiClient.apiClient.getInscriptionsByTeamId(Session.player.teamId.toString())

                    for(inscription in results.inscriptions){
                        if(inscription.tournamentId == loadedTournament.id){
                            registered = true
                            teamInscription = inscription
                            break
                        }
                    }

                    if(registered == true){
                        binding.btnToJoin.text = "Leave Tournament"
                    }else{
                        binding.btnToJoin.text = "Join Tournament"
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
            val i = Intent(this, PlayerActivity::class.java).apply {
                putExtra("PLAYER", admin)
            }
            startActivity(i)
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
                editedTournament = ApiClient.apiClient.getTournamentsById(editedTournament.id.toString())

                startActivity(Intent(this@TournamentActivity,TournamentActivity::class.java).apply {
                    putExtra("TOURNAMENT", editedTournament)
                })
            }
        }

        binding.btnToJoin.setOnClickListener(){
            if(registered == true){
                CoroutineScope(Dispatchers.IO).launch {
                    ApiClient.apiClient.deleteInscriptionsById(teamInscription.id.toString())
                    startActivity(Intent(this@TournamentActivity, TournamentActivity::class.java).apply {
                        putExtra("TOURNAMENT", loadedTournament)
                    })
                }
                //Delete inscription, reload activity
            }else{
                CoroutineScope(Dispatchers.IO).launch {
                    ApiClient.apiClient.postInscriptions(Inscription(
                        null,
                        Session.player.teamId.toString(),
                        loadedTournament.id.toString()
                    ))
                    startActivity(Intent(this@TournamentActivity, TournamentActivity::class.java).apply {
                        putExtra("TOURNAMENT", loadedTournament)
                    })
                }

            }
        }

        binding.btnDeleteTournament.setOnClickListener(){
            CoroutineScope(Dispatchers.IO).launch {
                var inscriptions = ApiClient.apiClient.getInscriptionsByTournamentId(loadedTournament.id.toString())
                for(inscription in inscriptions.inscriptions){
                    ApiClient.apiClient.deleteInscriptionsById(inscription.id.toString())
                }
            }

            CoroutineScope(Dispatchers.IO).launch {
                ApiClient.apiClient.deleteTournamentsById(loadedTournament.id.toString())
            }

            Session.update()
            startActivity(Intent(this, CreateActivity::class.java))
        }

        binding.btnToBackEdit.setOnClickListener(){
            binding.clToShow.visibility = View.VISIBLE
            binding.clToEdit.visibility = View.GONE
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                startActivity(Intent(this@TournamentActivity, SearchActivity::class.java))
            }
        })
    }

    private fun checkBtnToConfirmEditEnabled(){
        binding.btnToConfirmEdit.isEnabled = nameToOk && descToOk && prizeToOk
    }

    private fun setRecyclers(){
        val teamsLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.rvToTeams.layoutManager = teamsLayoutManager
        binding.rvToTeams.adapter = teamSquareAdapter
    }

    private fun showTeamInformation(team: Team) {
        Log.d("Enviando datos", "a TEAM PROFILE de " + team.name)
        val i = Intent(this, TeamActivity::class.java).apply {
            putExtra("TEAM", team)
        }

        startActivity(i)
    }
}