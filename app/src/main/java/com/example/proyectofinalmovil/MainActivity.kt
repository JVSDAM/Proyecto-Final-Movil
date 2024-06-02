package com.example.proyectofinalmovil

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import com.example.proyectofinalmovil.companions.Session
import com.example.proyectofinalmovil.databinding.ActivityMainBinding
import com.example.proyectofinalmovil.models.Player
import com.example.proyectofinalmovil.provider.ApiClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    lateinit var shared: SharedPreferences

    var nameRegisterOk = false
    var emailRegisterOk = false
    var passwordRegisterOk = false

    var emailLoginOk = false
    var passwordLoginOk = false

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

        shared = getSharedPreferences("Test", Context.MODE_PRIVATE)

        Log.d("Id shared", shared.getString("id", "").toString())

        checkSharedPreferences(shared.getString("id", "").toString())

        //Blurry.with(this).from(BitmapFactory.decodeResource(this.resources, R.drawable.baseline_person_24)).into(binding.imageView)

        /*Blurry.with(this)
            .radius(10)
            .sampling(8)
            .color(Color.argb(66, 255, 255, 0))
            .async()
            .onto(binding.clBackground);*/

        prepareForm()
        setListeners()
    }

    private fun checkSharedPreferences(sharedId: String) {
        if (sharedId != "") {
            CoroutineScope(Dispatchers.IO).launch {
                var results = ApiClient.apiClient.getPlayersById(sharedId)
                Session.sessionPlayer = results
                Log.d("Usuario que se logea", Session.sessionPlayer.name)
                joinApp()
            }
        }
    }

    private fun prepareForm() {
        binding.clRegister.visibility = View.GONE
        binding.clLogin.visibility = View.VISIBLE
        binding.btnRegister.isEnabled = false
        binding.btnLogin.isEnabled = false
    }

    private fun setListeners() {
        binding.etRegisterName.addTextChangedListener {
            nameRegisterOk = true

            if (binding.etRegisterName.text.toString() == "") {
                binding.etRegisterName.error = getText(R.string.errorEmptyField)
                nameRegisterOk = false
            } else {
                if (binding.etRegisterName.text.toString().length > 16) {
                    binding.etRegisterName.error = getText(R.string.errorTooLong)
                    nameRegisterOk = false
                }
            }

            checkBtnRegisterEnabled()
        }
        binding.etRegisterEmail.addTextChangedListener {
            emailRegisterOk = true

            if (binding.etRegisterEmail.text.toString() == "") {
                binding.etRegisterEmail.error = getText(R.string.errorEmptyField)
                emailRegisterOk = false
            } else {
                if (!Patterns.EMAIL_ADDRESS.matcher(
                        binding.etRegisterEmail.text.toString().trim()
                    ).matches()
                ) {
                    binding.etRegisterEmail.error = getText(R.string.errorNotValidEmail)
                    emailRegisterOk = false
                }

                checkBtnRegisterEnabled()
            }
        }
        binding.etRegisterPassword.addTextChangedListener {
            passwordRegisterOk = true

            if (binding.etRegisterPassword.text.toString().length < 6) {
                binding.etRegisterPassword.error = getText(R.string.errorTooShort)
                passwordRegisterOk = false
            }

            checkBtnRegisterEnabled()
        }
        binding.btnRegister.setOnClickListener {
            readRegisterForm()
        }
        binding.tvGoLogin.setOnClickListener {
            binding.clRegister.visibility = View.GONE
            binding.clLogin.visibility = View.VISIBLE
        }

        binding.etLoginEmail.addTextChangedListener {
            emailLoginOk = true

            if (binding.etLoginEmail.text.toString() == "") {
                binding.etLoginEmail.error = getText(R.string.errorEmptyField)
                emailLoginOk = false
            } else {
                if (!Patterns.EMAIL_ADDRESS.matcher(binding.etLoginEmail.text.toString().trim())
                        .matches()
                ) {
                    binding.etLoginEmail.error = getText(R.string.errorNotValidEmail)
                    emailLoginOk = false
                }
            }

            checkBtnLoginEnabled()
        }

        binding.etLoginPassword.addTextChangedListener {
            passwordLoginOk = true

            if (binding.etLoginPassword.text.toString().length < 6) {
                binding.etLoginPassword.error = getText(R.string.errorTooShort)
                passwordLoginOk = false
            }

            checkBtnLoginEnabled()
        }
        binding.btnLogin.setOnClickListener {
            readLoginForm()
        }
        binding.tvGoRegister.setOnClickListener {
            binding.clLogin.visibility = View.GONE
            binding.clRegister.visibility = View.VISIBLE
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finish()
            }
        })
    }

    private fun checkBtnRegisterEnabled() {
        binding.btnRegister.isEnabled = nameRegisterOk && emailRegisterOk && passwordRegisterOk
    }

    private fun checkBtnLoginEnabled() {
        binding.btnLogin.isEnabled = emailLoginOk && passwordLoginOk
    }

    private fun readRegisterForm() {
        CoroutineScope(Dispatchers.Main).launch {
            val result =
                ApiClient.apiClient.getPlayerByEmail(binding.etRegisterEmail.text.toString())
            if(result.isSuccessful){
                Log.d("Resultadito", result.body()!!.players.toString())

                if (result.body()!!.players.isNotEmpty()) {
                    binding.etRegisterEmail.error =
                        getText(R.string.errorAlreadyInUse)
                }else{
                    ApiClient.apiClient.postPlayers(Player(
                        id = null,
                        image = null,
                        name = binding.etRegisterName.text.toString().lowercase().replace(" ", "_"),
                        email = binding.etRegisterEmail.text.toString(),
                        password = binding.etRegisterPassword.text.toString(),
                        staff = null,
                        description = null,
                        account = null,
                        contact = null,
                        teamId = null
                    ))

                    Toast.makeText(this@MainActivity, "Player created! Log In to continue", Toast.LENGTH_SHORT).show()
                    binding.clRegister.visibility = View.GONE
                    binding.clLogin.visibility = View.VISIBLE
                }
            }else{
                Log.d(result.code().toString(), result.message())
            }

        }
    }

    private fun readLoginForm() {
        CoroutineScope(Dispatchers.Main).launch {
            val result =
                ApiClient.apiClient.getPlayerByEmail(binding.etLoginEmail.text.toString())
            Log.d("Resultadito", result.players.toString())
            if (result.players.size < 1) {
                binding.etLoginEmail.error = getText(R.string.errorEmailNotRegistered)
                emailLoginOk = false
            }else{
                applyLogIn()
            }
        }
    }

    private fun applyLogIn() {
        CoroutineScope(Dispatchers.Main).launch {
            var results = ApiClient.apiClient.getPlayerByEmail(binding.etLoginEmail.text.toString())
            if (results.players.size == 1) {

                if (results.players[0].password == binding.etLoginPassword.text.toString()) {
                    runOnUiThread {
                        Toast.makeText(
                            this@MainActivity,
                            "Logging in to the app...",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    binding.etLoginPassword
                    Session.sessionPlayer = results.players[0]
                    Log.d("Usuario que se logea", results.players.toString())

                    val sharedEmail = shared.edit()
                    sharedEmail.putString("id", results.players[0].id).apply()
                    Log.d("Id shared", shared.getString("id", "No user id").toString())
                    joinApp()
                } else {
                    Toast.makeText(this@MainActivity, "Incorrect password", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    private fun joinApp() {
        startActivity(Intent(this, MainMenuActivity::class.java))
    }
}
