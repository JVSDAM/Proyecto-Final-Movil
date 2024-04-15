package com.example.proyectofinalmovil

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.proyectofinalmovil.companions.Session
import com.example.proyectofinalmovil.databinding.ActivityMainBinding
import com.example.proyectofinalmovil.models.Player
import com.example.proyectofinalmovil.provider.ApiClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        prepareForm()
        setListeners()
    }

    private fun prepareForm() {
        binding.clRegister.visibility = View.GONE
        binding.clLogin.visibility = View.VISIBLE
    }

    private fun setListeners() {
        binding.btnRegister.setOnClickListener() {
            readRegisterForm()
        }
        binding.btnLogin.setOnClickListener() {
            readLoginForm()
        }
        binding.tvGoRegister.setOnClickListener() {
            binding.clLogin.visibility = View.GONE
            binding.clRegister.visibility = View.VISIBLE
        }
        binding.tvGoLogin.setOnClickListener() {
            binding.clRegister.visibility = View.GONE
            binding.clLogin.visibility = View.VISIBLE
        }
    }

    private fun readRegisterForm() {
        if (verifyRegisterForm()) {
            /*var playerTemp: Player = Player(
                "",
                binding.etRegisterName.text.toString(),
                ,
                binding.etRegisterEmail.text.toString(),
                binding.etRegisterPassword.text.toString(),
                ""
                "",
                "",
                ""
            )
            //createPlayer(playerTemp)*/
        } else {

        }
    }

    private fun verifyRegisterForm(): Boolean {
        return true
    }


    private fun readLoginForm() {
        if (verifyLoginForm()) {
            Toast.makeText(
                this,
                binding.etLoginEmail.text.toString() + " " + binding.etLoginPassword.text.toString(),
                Toast.LENGTH_SHORT
            ).show()
            //Login con la api
            applyLogIn()
        } else {
            Toast.makeText(this, "Error en la resolucion del formulario", Toast.LENGTH_SHORT).show()
        }

    }

    private fun verifyLoginForm(): Boolean {
        if (binding.etLoginEmail.text.toString() != "") {
            //TODO Patterns del correo

        } else if (binding.etLoginEmail.text.toString().length <= 16) {
            /*if(){
                TODO comprobar que el get de la api coincide con los datos introducidos
            }*/
        }

        return true
    }

    private fun applyLogIn(){
        CoroutineScope(Dispatchers.IO).launch {
            var results = ApiClient.apiClient.getPlayerByEmail(binding.etLoginEmail.text.toString())
            if (results.players.size == 1) {
                Log.d("Nota", "Se ha encontrado un jugador")
                if (results.players[0].password == binding.etLoginPassword.text.toString()) {
                    Log.d("Nota", "Log In Exitoso")
                    Session.player = results.players[0]
                    Log.d("ID DEL PLAYER", Session.player.id)
                    joinApp()
                } else {
                    Log.d("Error", "Contraseña incorrecta")
                }
            } else {
                Log.d("Error", "Jugador no encontrado")
            }

        }
    }

    private fun joinApp(){
        startActivity(Intent(this, PlayerActivity::class.java))
    }

    private suspend fun createPlayer(player: Player): Player? {
            try {
                val response = ApiClient.apiClient.postPlayers(player)
                if (response.isSuccessful) {
                    return response.body()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return null
        }
    }
