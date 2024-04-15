package com.example.proyectofinalmovil

import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.proyectofinalmovil.companions.Session
import com.example.proyectofinalmovil.databinding.ActivityPlayerBinding
import com.example.proyectofinalmovil.models.Player
import com.example.proyectofinalmovil.provider.ApiClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PlayerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPlayerBinding
    private lateinit var loadedPlayer: Player
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding =  ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        /*
        *  Para actualizar
        *             CoroutineScope(Dispatchers.IO).launch{
                var results = ApiClient.apiClient.getPlayersById(loadedPlayer.id)
                Session.player = results.players[0]
            }
        * */

        loadPlayer()
        fillInterface()
    }

    private fun loadPlayer(){
        if(intent.hasExtra("PLAYER")) {
            loadedPlayer = intent.getSerializableExtra("PLAYER") as Player
            binding.btnPEdit.visibility = View.GONE
        }else{
            loadedPlayer = Session.player
            binding.btnPEdit.visibility = View.VISIBLE
        }
    }

    private fun fillInterface(){
        Glide.with(binding.ivPImage.context).load(loadedPlayer.image).into(binding.ivPImage)
        binding.tvPName.text = loadedPlayer.name
        if(loadedPlayer.staff == true){
            binding.tvPStaff.text = "Staff"
        }else{
            binding.tvPStaff.text = "Player"
        }

        /*if(Session.player.description != null){
            binding.tvPDescription.text = loadedPlayer.description
        }else{
            binding.tvPDescription.text = "Tienes que añadir una descripcion canelo"
        }*/
        binding.tvPDescription.text = "Tienes que añadir una descripcion canelo"

        if(Session.player.account != ""){
            binding.tvPAccount.text = loadedPlayer.account
        }else{
            binding.tvPAccount.text = ""
        }

        /*if(Session.player.teamId != ""){
            binding.tvPDescription.text = loadedPlayer.name
        }else{
            binding.tvPDescription.text = ""
        }*/
    }
}