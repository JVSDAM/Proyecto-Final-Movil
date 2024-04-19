package com.example.proyectofinalmovil

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.activity.contextaware.withContextAvailable
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
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

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

        prepareForm()
        setListeners()
    }

    private fun prepareForm() {
        binding.clRegister.visibility = View.GONE
        binding.clLogin.visibility = View.VISIBLE
        binding.btnRegister.isEnabled = false
        binding.btnLogin.isEnabled = false
    }

    private fun setListeners() {
        binding.etRegisterName.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus == false) {
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
        }
        binding.etRegisterEmail.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus == false) {
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
                        emailLoginOk = false
                    } else {
                        CoroutineScope(Dispatchers.Main).launch {
                            val result =
                                ApiClient.apiClient.getPlayerByEmail(binding.etRegisterEmail.text.toString())
                            Log.d("Resultadito", result.players.toString())
                            if (result.players.size > 0) {
                                binding.etRegisterEmail.error =
                                    getText(R.string.errorAlreadyInUse)
                                emailRegisterOk = false
                            }
                        }
                    }

                    checkBtnRegisterEnabled()
                }
            }
        }
        binding.etRegisterPassword.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus == false) {
                passwordRegisterOk = true

                if (binding.etRegisterPassword.text.toString().length < 6) {
                    binding.etRegisterPassword.error = getText(R.string.errorTooShort)
                    passwordRegisterOk = false
                }

                checkBtnRegisterEnabled()
            }
        }
        binding.btnRegister.setOnClickListener {
            readRegisterForm()
        }
        binding.tvGoLogin.setOnClickListener {
            binding.clRegister.visibility = View.GONE
            binding.clLogin.visibility = View.VISIBLE
        }

        binding.etLoginEmail.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus == false) {
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
                    } else {
                        CoroutineScope(Dispatchers.Main).launch {
                            val result =
                                ApiClient.apiClient.getPlayerByEmail(binding.etLoginEmail.text.toString())
                            Log.d("Resultadito", result.players.toString())
                            if (result.players.size < 1) {
                                binding.etLoginEmail.error = getText(R.string.errorEmailNotRegistered)
                                emailLoginOk = false
                            }
                        }
                    }

                    checkBtnLoginEnabled()
                }
            }
        }
        binding.etLoginPassword.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus == false) {
                passwordLoginOk = true

                if (binding.etLoginPassword.text.toString().length < 6) {
                    binding.etLoginPassword.error = getText(R.string.errorTooShort)
                    passwordLoginOk = false
                }

                checkBtnLoginEnabled()
            }
        }
        binding.btnLogin.setOnClickListener {
            readLoginForm()
        }
        binding.tvGoRegister.setOnClickListener {
            binding.clLogin.visibility = View.GONE
            binding.clRegister.visibility = View.VISIBLE
        }
    }

    private fun checkBtnRegisterEnabled() {
        binding.btnRegister.isEnabled = nameRegisterOk && emailRegisterOk && passwordRegisterOk
    }

    private fun checkBtnLoginEnabled() {
        binding.btnLogin.isEnabled = emailLoginOk && passwordLoginOk
    }

    private fun readRegisterForm() {
        createPlayer(
            Player(
                id = null,
                image = null,
                name = binding.etRegisterName.text.toString().lowercase(),
                email = binding.etRegisterEmail.text.toString(),
                password = binding.etRegisterPassword.text.toString(),
                staff = null,
                description = null,
                account = null,
                contact = null,
                teamId = null
            )
        )
        Toast.makeText(this, "Player created! Log In to continue", Toast.LENGTH_SHORT).show()
        binding.clRegister.visibility = View.GONE
        binding.clLogin.visibility = View.VISIBLE
    }

    private fun createPlayer(player: Player) {
        CoroutineScope(Dispatchers.IO).launch {
            ApiClient.postPlayers(player)
        }
    }

    private fun readLoginForm() {
        applyLogIn()
    }

    private fun applyLogIn() {
        Toast.makeText(this, "Logging in to the app...", Toast.LENGTH_SHORT).show()
        CoroutineScope(Dispatchers.IO).launch {
            var results = ApiClient.apiClient.getPlayerByEmail(binding.etLoginEmail.text.toString())
            if (results.players.size == 1) {

                if (results.players[0].password == binding.etLoginPassword.text.toString()) {
                    Session.player = results.players[0]
                    Log.d("Usuario que se logea", results.players.toString())
                    joinApp()
                }
            }
        }
    }

    private fun joinApp() {
        startActivity(Intent(this, PlayerActivity::class.java))
    }
}
