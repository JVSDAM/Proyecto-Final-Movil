package com.example.proyectofinalmovil.fragments

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.proyectofinalmovil.MainActivity
import com.example.proyectofinalmovil.MainMenuActivity
import com.example.proyectofinalmovil.R
import com.example.proyectofinalmovil.adapters.TeamInviteAdapter
import com.example.proyectofinalmovil.companions.Session
import com.example.proyectofinalmovil.databinding.FragmentSettingsBinding
import com.example.proyectofinalmovil.models.Team
import com.example.proyectofinalmovil.provider.ApiClient
import com.example.proyectofinalmovil.viewmodel.MainMenuViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SettingsFragment : Fragment() {
    lateinit var binding: FragmentSettingsBinding

    lateinit var shared: SharedPreferences

    private var emailUOk = true
    private var passwordUOk = true
    private var passwordEditUOk = true

    private var emailUDelOk = true
    private var passwordUDelOk = true

    private val mainMenuVM: MainMenuViewModel by viewModels()

    val teamInviteAdapter = TeamInviteAdapter(mutableListOf(), { team -> showTeamInformation(team) })

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreate(savedInstanceState)
        binding = FragmentSettingsBinding.inflate(layoutInflater)

        shared = this.requireActivity().getSharedPreferences("Test", Context.MODE_PRIVATE)

        setRecyclers()

        mainMenuVM.teamList.observe(this.viewLifecycleOwner) {
            teamInviteAdapter.list = it
            teamInviteAdapter.notifyDataSetChanged()
        }

        fillInterface()
        setListeners()
        showView(binding.clStart)

        return binding.root
    }

    private fun showView(view: View) {
        binding.clStart.visibility = View.GONE
        binding.clEditUser.visibility = View.GONE
        binding.clDeleteUser.visibility = View.GONE
        binding.clTeamRequests.visibility = View.GONE

        view.visibility = View.VISIBLE
    }

    private fun fillInterface() {
        binding.etUEditEmail.setText(Session.sessionPlayer.email)

        CoroutineScope(Dispatchers.IO).launch {
            var inviteList = ApiClient.apiClient.getInvitesByPlayerId(Session.sessionPlayer.id.toString()).body()!!
            Log.d("Invitaciones a cargar", inviteList.invites.toString())
            mainMenuVM.searchInvites(inviteList)
        }
    }

    private fun setListeners() {
        binding.clBtnTeamRequests.setOnClickListener {
            showView(binding.clTeamRequests)
        }

        binding.clBtnEditUser.setOnClickListener {
            showView(binding.clEditUser)
        }

        binding.etUEditEmail.addTextChangedListener {
            emailUOk = true

            if (binding.etUEditEmail.text.toString() == "") {
                binding.etUEditEmail.error = getText(R.string.errorEmptyField)
                emailUOk = false
            } else {
                if (!Patterns.EMAIL_ADDRESS.matcher(binding.etUEditEmail.text.toString().trim())
                        .matches()
                ) {
                    binding.etUEditEmail.error = getText(R.string.errorNotValidEmail)
                    emailUOk = false
                }
            }

            checkBtnUConfirmEditEnabled()
        }

        binding.etUActualPassword.addTextChangedListener {
            passwordUOk = true

            if (binding.etUActualPassword.text.toString().length < 6) {
                binding.etUActualPassword.error = getText(R.string.errorTooShort)
                passwordUOk = false
            }

            checkBtnUConfirmEditEnabled()
        }

        binding.etUEditPassword.addTextChangedListener {
            passwordEditUOk = true
            if (binding.etUEditEmail.text.toString() != "" && binding.etUEditPassword.text.toString().length < 6) {
                binding.etUEditPassword.error = getText(R.string.errorTooShort)
                passwordEditUOk = false
            }

            checkBtnUConfirmEditEnabled()
        }

        binding.btnUConfirmEdit.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                var results = ApiClient.apiClient.getPlayersById(Session.sessionPlayer.id.toString()).body()!!

                if (results.password == binding.etUActualPassword.text.toString()) {

                    var newUser = Session.sessionPlayer

                    newUser.email = binding.etUEditEmail.text.toString()

                    if (binding.etUEditPassword.text.toString() != "") {
                        newUser.password = binding.etUEditPassword.text.toString()
                    }

                    ApiClient.apiClient.putPlayersById(Session.sessionPlayer.id.toString(), newUser)

                    shared.edit().clear().apply()
                    startActivity(Intent(context, MainActivity::class.java))
                }
            }
        }

        binding.clBtnDeleteUser.setOnClickListener {
            showView(binding.clDeleteUser)
        }

        binding.etDeletePass.addTextChangedListener {
            passwordUDelOk = true

            if (binding.etDeletePass.text.toString().length < 6) {
                binding.etDeletePass.error = getText(R.string.errorTooShort)
                passwordUDelOk = false
            }

            checkBtnUConfirmDeleteEnabled()
        }

        binding.btnDeleteUser.setOnClickListener {
            if (binding.etDeleteEmail.text.toString() == Session.sessionPlayer.email) {
                if (binding.etDeletePass.text.toString() == Session.sessionPlayer.password) {
                    CoroutineScope(Dispatchers.IO).launch {
                        ApiClient.apiClient.deletePlayersById(Session.sessionPlayer.id.toString())
                    }
                    logOut()
                } else {
                    binding.etDeletePass.error = getText(R.string.errorNotValidPassword)
                }
            } else {
                binding.etDeleteEmail.error = getText(R.string.errorNotValidEmail)
            }

        }

        binding.clBtnLogOut.setOnClickListener {
            logOut()
        }

        binding.btnBackEditUser.setOnClickListener{
            showView(binding.clStart)
        }

        binding.btnBackDeleteUser.setOnClickListener{
            showView(binding.clStart)
        }

        binding.btnBackTeamInvites.setOnClickListener{
            showView(binding.clStart)
        }
    }

    private fun setRecyclers(){
        val teamInviteLayoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.rvInvites.layoutManager = teamInviteLayoutManager
        binding.rvInvites.adapter = teamInviteAdapter
    }

    private fun showTeamInformation(team: Team){
        Session.changeLoadedTeam(team)

        (activity as MainMenuActivity).replaceFragments(TeamFragment())
    }

    private fun checkBtnUConfirmEditEnabled() {
        binding.btnUConfirmEdit.isEnabled = emailUOk && passwordUOk && passwordEditUOk
    }

    private fun checkBtnUConfirmDeleteEnabled() {
        binding.btnDeleteUser.isEnabled = emailUDelOk && passwordUDelOk
    }

    private fun logOut() {
        shared.edit().clear().apply()
        startActivity(Intent(context, MainActivity::class.java))
    }
}