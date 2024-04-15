package com.example.proyectofinalmovil

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.proyectofinalmovil.companions.Session
import com.example.proyectofinalmovil.databinding.ActivityMainBinding
import com.example.proyectofinalmovil.models.PlayerPOJO

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

    private fun prepareForm(){
        binding.clRegister.visibility = View.GONE
        binding.clLogin.visibility = View.VISIBLE
    }

    private fun setListeners(){
        binding.btnRegister.setOnClickListener(){
            readRegisterForm()
        }
        binding.btnLogin.setOnClickListener(){
            readLoginForm()
        }
        binding.tvGoRegister.setOnClickListener(){
            binding.clLogin.visibility = View.GONE
            binding.clRegister.visibility = View.VISIBLE
        }
        binding.tvGoLogin.setOnClickListener(){
            binding.clRegister.visibility = View.GONE
            binding.clLogin.visibility = View.VISIBLE
        }
    }

    private fun readRegisterForm(){
        if(verifyRegisterForm()) {

        } else{

        }
    }

    private fun verifyRegisterForm(): Boolean{
        return true
    }


    private fun readLoginForm(){
        if(verifyLoginForm()){
            Toast.makeText(this, binding.etLoginNameOrEmail.text.toString() + " " + binding.etLoginPassword.text.toString(), Toast.LENGTH_SHORT).show()
            //TODO login con la api
            Session.player = PlayerPOJO("name", "email", "password", "account", "team_id")
            startActivity(Intent(this, PlayerActivity::class.java))

        } else{
            Toast.makeText(this, "Error en la resolucion del formulario", Toast.LENGTH_SHORT).show()
        }

    }

    private fun verifyLoginForm(): Boolean{
        if(binding.etLoginNameOrEmail.text.toString() != ""){
            //TODO Patterns del correo
        }else if(binding.etLoginNameOrEmail.text.toString().length <= 16){
            /*if(){
                TODO comprobar que el get de la api coincide con los datos introducidos
            }*/
        }

        return true
    }
}