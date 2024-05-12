package com.example.proyectofinalmovil

import android.content.Intent
import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import com.bumptech.glide.Glide
import com.example.proyectofinalmovil.companions.Session
import com.example.proyectofinalmovil.databinding.ActivityPlayerBinding
import com.example.proyectofinalmovil.models.Player
import com.example.proyectofinalmovil.models.Team
import com.example.proyectofinalmovil.provider.ApiClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PlayerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPlayerBinding
    private lateinit var loadedPlayer: Player
    private lateinit var team: Team

    private var inYourTeam = false

    private var namePOk = true
    private var descPOk = true
    private var accountPOk = true
    private var contactPOk = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        loadPlayer()
        fillInterface()
        setListeners()
    }

    private fun loadPlayer(){
        if(intent.hasExtra("PLAYER")) {
            loadedPlayer = intent.getSerializableExtra("PLAYER") as Player
            checkInYourTeam()
        }else{
            Session.update()
            loadedPlayer = Session.player
        }
        Log.d("Loaded player", loadedPlayer.toString())
    }

    private fun checkInYourTeam(){
        if(Session.player.teamId.toString() != "" && Session.player.teamId.toString() != null) {
            if (loadedPlayer.teamId.toString() != "" && loadedPlayer.teamId.toString() != null) {
                if(loadedPlayer.teamId.toString() == Session.player.teamId.toString()){
                    CoroutineScope(Dispatchers.IO).launch{
                        var yourTeam = ApiClient.apiClient.getTeamsById(Session.player.teamId.toString())
                        if (yourTeam.adminId == Session.player.id){
                            inYourTeam = true
                        }
                    }
                }
            }
        }
    }

    private fun fillInterface() {
        Glide.with(binding.ivPImage.context).load(loadedPlayer.image).into(binding.ivPImage)

        binding.tvPName.text = loadedPlayer.name
        if (loadedPlayer.staff == true) {
            binding.tvPStaff.text = "Staff"
        } else {
            binding.tvPStaff.text = "Player"
        }

        if (loadedPlayer.description != "") {
            binding.tvPDescription.text = loadedPlayer.description
        } else {
            binding.tvPDescription.text = getText(R.string.errorNoDescription)
        }

        if (loadedPlayer.account != "") {
            binding.tvPAccount.text = loadedPlayer.account
        } else {
            binding.tvPAccount.text = getText(R.string.errorNoAccount)
        }

        if (loadedPlayer.contact != "") {
            binding.tvPContact.text = loadedPlayer.contact
        } else {
            binding.tvPContact.text = getText(R.string.errorNoContact)
        }

        binding.btnPEdit.visibility = View.GONE
        if(loadedPlayer.id == Session.player.id){
            binding.btnPEdit.visibility = View.VISIBLE
        }

        if (loadedPlayer.teamId != "") {
            CoroutineScope(Dispatchers.IO).launch {
                team = ApiClient.apiClient.getTeamsById(loadedPlayer.teamId.toString())
                //Glide.with(binding.ivPTeam.context).load(team.image).into(binding.ivPTeam)
                binding.tvPTeam.text = team.name
                Log.d("Equipo cargado", team.toString())
            }


        } else {
            binding.constraintTeam.visibility = View.GONE
        }

        binding.etPEditName.setText(loadedPlayer.name)

        binding.cbPEditStaff.isChecked = loadedPlayer.staff!!

        binding.etPEditDescription.setText(loadedPlayer.description)

        binding.etPEditAccount.setText(loadedPlayer.account)

        binding.etPEditContact.setText(loadedPlayer.contact)

        if(inYourTeam == true){
            binding.btnKickPlayer.visibility = View.VISIBLE
            binding.btnKickPlayer.text = "Leave Team"
        }else{
            binding.btnKickPlayer.visibility = View.GONE
            binding.btnKickPlayer.text = "Join Team"
        }
    }

    private fun loadTeamImage(){
        Glide.with(binding.ivPTeam.context).load(team.image).into(binding.ivPTeam)
    }

    private fun setListeners(){
        binding.constraintTeam.setOnClickListener(){
            if(loadedPlayer.teamId != null){
                val i = Intent(this, TeamActivity::class.java).apply {
                    putExtra("TEAM", team)
                }
                startActivity(i)
            }
        }

        binding.btnPEdit.setOnClickListener(){
            binding.clPShow.visibility = View.GONE
            binding.clPEdit.visibility = View.VISIBLE
        }

        binding.etPEditName.addTextChangedListener {
            namePOk = true

            if (binding.etPEditName.text.toString() == "") {
                binding.etPEditName.error = getText(R.string.errorEmptyField)
                namePOk = false
            } else {
                if (binding.etPEditName.text.toString().length > 16) {
                    binding.etPEditName.error = getText(R.string.errorTooLong)
                    namePOk = false
                }
            }

            checkBtnPConfirmEditEnabled()
        }

        binding.etPEditDescription.addTextChangedListener {
            descPOk = true

            if (binding.etPEditDescription.text.toString().length > 60) {
                binding.etPEditDescription.error = getText(R.string.errorTooLong)
                descPOk = false
            }

            checkBtnPConfirmEditEnabled()
        }

        binding.etPEditAccount.addTextChangedListener {
            accountPOk = true

            if (binding.etPEditAccount.text.toString().length > 22) {
                binding.etPEditAccount.error = getText(R.string.errorTooLong)
                namePOk = false
            }


            checkBtnPConfirmEditEnabled()
        }

        binding.etPEditContact.addTextChangedListener {
            contactPOk = true
            if (binding.etPEditName.text.toString().length > 22) {
                binding.etPEditName.error = getText(R.string.errorTooLong)
                namePOk = false
            }

            checkBtnPConfirmEditEnabled()
        }

        binding.btnPConfirmEdit.setOnClickListener(){
            CoroutineScope(Dispatchers.IO).launch {
                var editedPlayer = loadedPlayer

                editedPlayer.name = binding.etPEditName.text.toString()
                editedPlayer.description = binding.etPEditDescription.text.toString()
                editedPlayer.staff = binding.cbPEditStaff.isChecked
                editedPlayer.account = binding.etPEditAccount.text.toString()
                editedPlayer.contact = binding.etPEditContact.text.toString()


                ApiClient.apiClient.putPlayersById(editedPlayer.id.toString(), editedPlayer)
                loadedPlayer = ApiClient.apiClient.getPlayersById(editedPlayer.id.toString())

                startActivity(Intent(this@PlayerActivity,PlayerActivity::class.java).apply {
                    putExtra("PLAYER", loadedPlayer)
                })
            }
        }

        binding.btnPBackEdit.setOnClickListener(){
            binding.clPShow.visibility = View.VISIBLE
            binding.clPEdit.visibility = View.GONE
        }
    }

    private fun checkBtnPConfirmEditEnabled(){
        binding.btnPConfirmEdit.isEnabled = namePOk && descPOk && accountPOk && contactPOk
        Log.d(binding.btnPConfirmEdit.isEnabled.toString(), namePOk.toString() + descPOk.toString() + accountPOk.toString() + contactPOk.toString())
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if(intent.hasExtra("PLAYER")) {
            startActivity(Intent(this, SearchActivity::class.java))
        }else{
            super.onBackPressed()
            finishAffinity()
        }

    }
}