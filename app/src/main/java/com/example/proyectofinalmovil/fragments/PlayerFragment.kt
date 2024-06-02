package com.example.proyectofinalmovil.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.proyectofinalmovil.MainMenuActivity
import com.example.proyectofinalmovil.R
import com.example.proyectofinalmovil.companions.Session
import com.example.proyectofinalmovil.databinding.FragmentPlayerBinding
import com.example.proyectofinalmovil.models.Invite
import com.example.proyectofinalmovil.models.Player
import com.example.proyectofinalmovil.models.Team
import com.example.proyectofinalmovil.provider.ApiClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PlayerFragment : Fragment() {
    private lateinit var binding: FragmentPlayerBinding
    private lateinit var loadedPlayer: Player
    private lateinit var team: Team

    private var inYourTeam = false

    private var namePOk = true
    private var descPOk = true
    private var accountPOk = true
    private var contactPOk = true

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View{
        super.onCreate(savedInstanceState)
        binding = FragmentPlayerBinding.inflate(layoutInflater)

        loadPlayer()
        fillInterface()
        setListeners()
        return binding.root
    }

    private fun loadPlayer(){
        //Log.d("Player in fragment", loadedPlayer.toString())
        loadedPlayer = Session.loadedPlayer!!
        if(loadedPlayer.id != Session.sessionPlayer.id) {
            checkInYourTeam()
        }

    }

    private fun checkInYourTeam(){
        if(Session.sessionPlayer.teamId.toString() != "" && Session.sessionPlayer.teamId.toString() != null) {

            CoroutineScope(Dispatchers.IO).launch {
                var yourTeam = ApiClient.apiClient.getTeamsById(Session.sessionPlayer.teamId.toString())
                if (yourTeam.adminId == Session.sessionPlayer.id) {
                    if (loadedPlayer.teamId.toString() != "" && loadedPlayer.teamId.toString() != null) {
                        if (loadedPlayer.teamId.toString() == Session.sessionPlayer.teamId.toString()) {
                            inYourTeam = true
                            binding.btnKickInvitePlayer.visibility = View.VISIBLE
                            binding.btnKickInvitePlayer.text = getText(R.string.btnKickPlayer)
                            binding.btnKickInvitePlayer.icon = resources.getDrawable(R.drawable.baseline_no_accounts_24)
                        }
                    }else{
                        inYourTeam = false
                        binding.btnKickInvitePlayer.visibility = View.VISIBLE
                        binding.btnKickInvitePlayer.text = getText(R.string.btnInvitePlayer)
                        binding.btnKickInvitePlayer.icon = resources.getDrawable(R.drawable.baseline_person_add_alt_1_24)
                    }
                }
            }
        }
    }

    private fun fillInterface() {
        Glide.with(binding.ivPImage.context).load(loadedPlayer.image).into(binding.ivPImage)

        binding.tvPName.text = loadedPlayer.name
        if (loadedPlayer.staff == true) {
            binding.tvPStaff.text = getText(R.string.textStaff)
        } else {
            binding.tvPStaff.text = getText(R.string.textPlayer)
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
        if(loadedPlayer.id == Session.sessionPlayer.id){
            binding.btnPEdit.visibility = View.VISIBLE
        }

        binding.cvShowTeam.visibility = View.GONE
        if (loadedPlayer.teamId != "") {
            CoroutineScope(Dispatchers.Main).launch {
                team = ApiClient.apiClient.getTeamsById(loadedPlayer.teamId.toString())
                Glide.with(binding.ivPTeam).load(team.image).into(binding.ivPTeam)
                binding.tvPTeam.text = team.name
                Log.d("Equipo cargado", team.toString())
                binding.cvShowTeam.visibility = View.VISIBLE
            }
        }

        binding.btnKickInvitePlayer.visibility = View.GONE
        binding.btnKickInvitePlayer.elevation = 0f


        binding.etPEditName.setText(loadedPlayer.name)

        binding.cbPEditStaff.isChecked = loadedPlayer.staff!!

        binding.etPEditDescription.setText(loadedPlayer.description)

        binding.etPEditAccount.setText(loadedPlayer.account)

        binding.etPEditContact.setText(loadedPlayer.contact)
    }

    private fun loadTeamImage(){
        Glide.with(binding.ivPTeam.context).load(team.image).into(binding.ivPTeam)
    }

    private fun setListeners(){
        binding.cvShowTeam.setOnClickListener(){
            if(loadedPlayer.teamId != null){
                Log.d("Equipo a cargar", team.toString())
                Session.changeLoadedTeam(team)

                (activity as MainMenuActivity).replaceFragments(TeamFragment())
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

                Session.changeLoadedPlayer(loadedPlayer)

                (activity as MainMenuActivity).replaceFragments(PlayerFragment())
            }
        }

        binding.btnPBackEdit.setOnClickListener(){
            binding.clPShow.visibility = View.VISIBLE
            binding.clPEdit.visibility = View.GONE
        }

        binding.btnKickInvitePlayer.setOnClickListener(){
            if(inYourTeam){
                CoroutineScope(Dispatchers.Main).launch {
                    loadedPlayer.teamId = ""
                    ApiClient.apiClient.putPlayersById(loadedPlayer.id.toString(), loadedPlayer)

                    Session.changeLoadedPlayer(loadedPlayer)

                    (activity as MainMenuActivity).replaceFragments(PlayerFragment())
                }
            }else{
                CoroutineScope(Dispatchers.Main).launch {
                    var results = ApiClient.apiClient.getInvitesByPlayerId(loadedPlayer.id.toString())
                    var canInvite = true
                    for(invite in results.invites){
                        if(invite.teamId == Session.sessionPlayer.teamId){
                            canInvite = false
                            binding.btnKickInvitePlayer.setText(getText(R.string.btnInvitePending))
                            binding.btnKickInvitePlayer.icon = resources.getDrawable(R.drawable.baseline_access_time_24)

                            break
                        }
                    }

                    if(canInvite){
                        ApiClient.apiClient.postInvites(Invite(
                            null,
                            Session.sessionPlayer.teamId.toString(),
                            loadedPlayer.id.toString()
                        ))
                    }
                }
            }
        }
    }

    private fun checkBtnPConfirmEditEnabled(){
        binding.btnPConfirmEdit.isEnabled = namePOk && descPOk && accountPOk && contactPOk
        Log.d(binding.btnPConfirmEdit.isEnabled.toString(), namePOk.toString() + descPOk.toString() + accountPOk.toString() + contactPOk.toString())
    }
}