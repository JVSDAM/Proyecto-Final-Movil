package com.example.proyectofinalmovil.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.proyectofinalmovil.MainMenuActivity
import com.example.proyectofinalmovil.R
import com.example.proyectofinalmovil.companions.Session
import com.example.proyectofinalmovil.databinding.FragmentCreateBinding
import com.example.proyectofinalmovil.models.Team
import com.example.proyectofinalmovil.models.Tournament
import com.example.proyectofinalmovil.provider.ApiClient
import com.example.proyectofinalmovil.viewmodel.MainMenuViewModel
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CreateFragment : Fragment() {
    private lateinit var binding: FragmentCreateBinding

    private var nameTOk: Boolean = false
    private var descTOk: Boolean = false

    private var nameToOk: Boolean = false
    private var descToOk: Boolean = false
    private var prizeToOk: Boolean = false

    private val mainMenuVM: MainMenuViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreate(savedInstanceState)
        binding = FragmentCreateBinding.inflate(layoutInflater)

        prepareForm()
        setListeners()

        changeForm(1)

        return binding.root
    }

    private fun prepareForm() {
        binding.clCTournament.visibility = View.GONE
        binding.clCTeam.visibility = View.VISIBLE
        binding.btnCTournament.isEnabled = false
        binding.btnCTeam.isEnabled = false
    }

    private fun setListeners() {
        binding.tlCreate.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(p0: TabLayout.Tab?) {
                if (p0 == binding.tlCreate.getTabAt(0)) {
                    changeForm(1)
                }
                if (p0 == binding.tlCreate.getTabAt(1)) {
                    changeForm(2)
                }
            }

            override fun onTabUnselected(p0: TabLayout.Tab?) {
                //No necesario pero requerido
            }

            override fun onTabReselected(p0: TabLayout.Tab?) {
                //No necesario pero requerido
            }
        })

        binding.etCTName.addTextChangedListener {
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

        binding.etCTDescription.addTextChangedListener {
            descTOk = true

            if (binding.etCTDescription.text.toString().length > 60) {
                binding.etCTDescription.error = getText(R.string.errorTooLong)
                descTOk = false
            }

            checkBtnCTeamEnabled()
        }

        binding.btnCTeam.setOnClickListener {
            if (Session.sessionPlayer.teamId == "" || Session.sessionPlayer.teamId == null) {
                readCTForm()
            } else {
                Toast.makeText(context, getText(R.string.errorAlreadyInTeam), Toast.LENGTH_SHORT)
                    .show()
            }
        }

        binding.etCToName.addTextChangedListener {
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

        binding.etCToDescription.addTextChangedListener {
            descToOk = true

            if (binding.etCToDescription.text.toString().length > 60) {
                binding.etCToDescription.error = getText(R.string.errorTooLong)
                descToOk = false
            }

            checkBtnCTournamentEnabled()
        }

        binding.etCToPrize.addTextChangedListener {
            prizeToOk = true

            if (binding.etCToPrize.text.toString().length > 60) {
                binding.etCToPrize.error = getText(R.string.errorTooLong)
                prizeToOk = false
            }

            checkBtnCTournamentEnabled()
        }

        binding.btnCTournament.setOnClickListener {
            readCToForm()
        }
    }

    private fun changeForm(number: Int) {
        binding.clCTeam.visibility = View.GONE
        binding.clCTournament.visibility = View.GONE

        if (number == 1) {
            binding.clCTeam.visibility = View.VISIBLE
        }
        if (number == 2) {
            binding.clCTournament.visibility = View.VISIBLE
        }
    }

    private fun checkBtnCTeamEnabled() {
        binding.btnCTeam.isEnabled = nameTOk && descTOk
    }

    private fun checkBtnCTournamentEnabled() {
        binding.btnCTournament.isEnabled = nameToOk && descToOk && prizeToOk
    }

    private fun readCTForm() {
        createTeam(
            Team(
                id = null,
                image = null,
                name = binding.etCTName.text.toString().lowercase().replace(" ", "_"),
                description = binding.etCTDescription.text.toString(),
                adminId = Session.sessionPlayer.id.toString(),
            )
        )
        Toast.makeText(context, getText(R.string.teamCreated), Toast.LENGTH_SHORT).show()
    }

    private fun readCToForm() {
        createTournament(
            Tournament(
                id = null,
                image = null,
                name = binding.etCToName.text.toString().lowercase().replace(" ", "_"),
                description = binding.etCToDescription.text.toString(),
                prize = binding.etCToPrize.text.toString(),
                adminId = Session.sessionPlayer.id.toString(),
            )
        )
        Toast.makeText(context, getText(R.string.tournamentCreated), Toast.LENGTH_SHORT).show()
    }

    private fun createTeam(team: Team) {
        CoroutineScope(Dispatchers.IO).launch {
            var newTeam = ApiClient.apiClient.postTeams(team)
            var uPlayer = Session.sessionPlayer
            if (newTeam.body() != null) {
                newTeam.code()
                uPlayer.teamId = newTeam.body()!!.id.toString()
                ApiClient.apiClient.putPlayersById(Session.sessionPlayer.id.toString(), uPlayer)
                Session.update()
            }

            Session.loadedTeam = team

            (activity as MainMenuActivity).replaceFragments(TeamFragment())
        }
    }

    private fun createTournament(tournament: Tournament) {
        CoroutineScope(Dispatchers.IO).launch {
            ApiClient.apiClient.postTournaments(tournament)

            Session.loadedTournament = tournament

            (activity as MainMenuActivity).replaceFragments(TournamentFragment())
        }
    }
}