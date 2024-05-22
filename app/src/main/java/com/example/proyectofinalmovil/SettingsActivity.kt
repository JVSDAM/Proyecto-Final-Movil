package com.example.proyectofinalmovil

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.proyectofinalmovil.adapters.TeamInviteAdapter
import com.example.proyectofinalmovil.companions.Session
import com.example.proyectofinalmovil.databinding.ActivitySettingsBinding
import com.example.proyectofinalmovil.models.Team
import com.example.proyectofinalmovil.provider.ApiClient
import com.example.proyectofinalmovil.viewmodel.TeamInvitesViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SettingsActivity : AppCompatActivity() {
    lateinit var binding: ActivitySettingsBinding

    lateinit var shared: SharedPreferences

    private var emailUOk = true
    private var passwordUOk = true
    private var passwordEditUOk = true

    private var emailUDelOk = true
    private var passwordUDelOk = true

    private val teamInvitesVM: TeamInvitesViewModel by viewModels()
    val teamInviteAdapter = TeamInviteAdapter(mutableListOf(), { team -> showTeamInformation(team) })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        shared = getSharedPreferences("Test", Context.MODE_PRIVATE)

        setRecyclers()

        teamInvitesVM.teamList.observe(this) {
            teamInviteAdapter.list = it
            teamInviteAdapter.notifyDataSetChanged()
        }

        fillInterface()
        setListeners()
        showView(binding.clStart)
    }

    private fun showView(view: View) {
        binding.clStart.visibility = View.GONE
        binding.clEditUser.visibility = View.GONE
        binding.clDeleteUser.visibility = View.GONE
        binding.clTeamRequests.visibility = View.GONE

        view.visibility = View.VISIBLE
    }

    private fun fillInterface() {
        binding.etUEditEmail.setText(Session.player.email)

        CoroutineScope(Dispatchers.IO).launch {
            var inviteList = ApiClient.apiClient.getInvitesByPlayerId(Session.player.id.toString())
            Log.d("Invitaciones a cargar", inviteList.invites.toString())
            teamInvitesVM.searchInvites(inviteList)
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
                var results = ApiClient.apiClient.getPlayersById(Session.player.id.toString())

                if (results.password == binding.etUActualPassword.text.toString()) {

                    var newUser = Session.player

                    newUser.email = binding.etUEditEmail.text.toString()

                    if (binding.etUEditPassword.text.toString() != "") {
                        newUser.password = binding.etUEditPassword.text.toString()
                    }

                    ApiClient.apiClient.putPlayersById(Session.player.id.toString(), newUser)

                    shared.edit().clear().apply()
                    startActivity(Intent(this@SettingsActivity, MainActivity::class.java))
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
            if (binding.etDeleteEmail.text.toString() == Session.player.email) {
                if (binding.etDeletePass.text.toString() == Session.player.password) {
                    CoroutineScope(Dispatchers.IO).launch {
                        ApiClient.apiClient.deletePlayersById(Session.player.id.toString())
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

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (binding.clStart.visibility == View.GONE) {
                    showView(binding.clStart)
                } else {
                    startActivity(Intent(this@SettingsActivity, PlayerActivity::class.java))
                }

            }
        })
    }

    private fun setRecyclers(){
        val teamInviteLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.rvInvites.layoutManager = teamInviteLayoutManager
        binding.rvInvites.adapter = teamInviteAdapter
    }

    private fun showTeamInformation(team: Team){
        Log.d("Enviando datos", "a TEAM PROFILE de " + team.name)
        val i = Intent(this, TeamActivity::class.java).apply {
            putExtra("TEAM", team)
        }

        startActivity(i)
    }

    private fun checkBtnUConfirmEditEnabled() {
        binding.btnUConfirmEdit.isEnabled = emailUOk && passwordUOk && passwordEditUOk
    }

    private fun checkBtnUConfirmDeleteEnabled() {
        binding.btnDeleteUser.isEnabled = emailUDelOk && passwordUDelOk
    }

    private fun logOut() {
        shared.edit().clear().apply()
        startActivity(Intent(this, MainActivity::class.java))
    }
}