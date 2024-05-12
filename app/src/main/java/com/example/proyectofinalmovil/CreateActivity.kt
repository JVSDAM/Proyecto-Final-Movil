package com.example.proyectofinalmovil

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.proyectofinalmovil.companions.Session
import com.example.proyectofinalmovil.databinding.ActivityCreateBinding
import com.example.proyectofinalmovil.models.Player
import com.example.proyectofinalmovil.models.Team
import com.example.proyectofinalmovil.models.Tournament
import com.example.proyectofinalmovil.provider.ApiClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CreateActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateBinding

    private var nameTOk: Boolean = false
    private var descTOk: Boolean = false
    private var nameToOk: Boolean = false
    private var descToOk: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityCreateBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setListeners()

        changeForm(1)
    }

    private fun setListeners(){
        binding.btnFormTeam.setOnClickListener(){
            changeForm(1)
        }

        binding.etCTName.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus == false) {
                nameTOk = true

                if (binding.etCTName.text.toString() == "") {
                    binding.etCTName.error = getText(R.string.errorEmptyField)
                    nameTOk = false
                } else {
                    if (binding.etCTName.text.toString().length > 16) {
                        binding.etCTName.error = getText(R.string.errorTooLong)
                        nameTOk = false
                    }
                }

                checkBtnCTeamEnabled()
            }
        }

        binding.etCTDescription.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus == false) {
                descTOk = true

                if (binding.etCTDescription.text.toString().length > 60) {
                    binding.etCTDescription.error = getText(R.string.errorTooLong)
                    descTOk = false
                }

                checkBtnCTeamEnabled()
            }
        }
        
        binding.btnCTeam.setOnClickListener(){
            if(Session.player.teamId == "" || Session.player.teamId == null){
                readCTForm()
            }else {
                Toast.makeText(this, "You already have a team!", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnFormTournament.setOnClickListener(){
            changeForm(2)
        }

        binding.etCToName.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus == false) {
                nameToOk = true

                if (binding.etCToName.text.toString() == "") {
                    binding.etCToName.error = getText(R.string.errorEmptyField)
                    nameToOk = false
                } else {
                    if (binding.etCToName.text.toString().length > 16) {
                        binding.etCToName.error = getText(R.string.errorTooLong)
                        nameToOk = false
                    }
                }

                checkBtnCTournamentEnabled()
            }
        }

        binding.etCToDescription.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus == false) {
                descToOk = true

                if (binding.etCToDescription.text.toString().length > 60) {
                    binding.etCToDescription.error = getText(R.string.errorTooLong)
                    descToOk = false
                }

                checkBtnCTournamentEnabled()
            }
        }

        binding.btnCTournament.setOnClickListener(){
            readCToForm()
        }
    }

    private fun changeForm(number: Int){
        binding.btnCTeam.setBackgroundColor(Color.parseColor("#FFE1C53A"))
        binding.btnCTournament.setBackgroundColor(Color.parseColor("#FFE1C53A"))

        binding.clCTeam.visibility = View.GONE
        binding.clCTournament.visibility = View.GONE

        if(number == 1){
            binding.btnCTeam.setBackgroundColor(Color.parseColor("#FF806F1F"))
            binding.clCTeam.visibility = View.VISIBLE
        }
        if(number == 2){
            binding.btnCTournament.setBackgroundColor(Color.parseColor("#FF806F1F"))
            binding.clCTournament.visibility = View.VISIBLE
        }
    }

    private fun checkBtnCTeamEnabled(){
        binding.btnCTeam.isEnabled = nameTOk && descTOk
    }

    private fun checkBtnCTournamentEnabled(){
        binding.btnCTournament.isEnabled = nameToOk && descToOk
    }

    private fun readCTForm(){
        createTeam(
            Team(
                id = null,
                image = null,
                name = binding.etCTName.text.toString().lowercase().replace(" ", "_"),
                description = binding.etCTDescription.text.toString(),
                adminId = Session.player.id.toString(),
            )
        )
        Toast.makeText(this, "Team created!", Toast.LENGTH_SHORT).show()
    }

    private fun readCToForm(){
        createTournament(
            Tournament(
                id = null,
                image = null,
                name = binding.etCToName.text.toString().lowercase().replace(" ", "_"),
                description = binding.etCToDescription.text.toString(),
                prize = binding.etCToPrize.text.toString(),
                adminId = Session.player.id.toString(),
            )
        )
        Toast.makeText(this, "Tournament created!", Toast.LENGTH_SHORT).show()
    }

    private fun createTeam(team: Team){
        CoroutineScope(Dispatchers.IO).launch {
            var newTeam = ApiClient.postTeams(team)
            var uPlayer = Session.player
            if (newTeam != null) {
                uPlayer.teamId = newTeam.id.toString()
                ApiClient.apiClient.putPlayersById(Session.player.id.toString(),uPlayer)
                Session.update()
            }
        }
    }

    private fun createTournament(tournament: Tournament){
        CoroutineScope(Dispatchers.IO).launch {
            ApiClient.postTournaments(tournament)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, PlayerActivity::class.java))
    }
}