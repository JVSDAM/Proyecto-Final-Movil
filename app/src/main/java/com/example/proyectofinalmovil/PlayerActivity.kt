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
import com.example.proyectofinalmovil.models.Team
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
        binding = ActivityPlayerBinding.inflate(layoutInflater)
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
            Log.d("Loaded player", loadedPlayer.toString())
        }else{
            loadedPlayer = Session.player
            binding.btnPEdit.visibility = View.VISIBLE
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

        if (Session.player.teamId != "") {
            CoroutineScope(Dispatchers.IO).launch {
                var team = ApiClient.apiClient.getTeamsById(loadedPlayer.teamId.toString())
                //TODO tengo que llamar esto desde el metodo principal para cargar la imagen
                /*val futureTarget: FutureTarget<Bitmap> = Glide.with(context)
    .asBitmap()
    .load(imageUrl)
    .submit(width, height)

// Do something with the Bitmap (e.g., display it in an ImageView)
val bitmap: Bitmap? = futureTarget.get()

// When you're done with the Bitmap, clear the target
Glide.with(context).clear(futureTarget)

Probar a implementar este sistema
*/
                //Glide.with(binding.ivPTeam.context).load(team.image).into(binding.ivPTeam)
                binding.tvPTeam.text = team.name
                Log.d("Equipo cargado", team.toString())
            }


        } else {
            binding.tvPTeam.text = getText(R.string.errorNoTeam)
        }


    }
}