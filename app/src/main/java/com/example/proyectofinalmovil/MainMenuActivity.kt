package com.example.proyectofinalmovil

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.proyectofinalmovil.companions.Session
import com.example.proyectofinalmovil.databinding.ActivityMainMenuBinding
import com.example.proyectofinalmovil.fragments.CreateFragment
import com.example.proyectofinalmovil.fragments.PlayerFragment
import com.example.proyectofinalmovil.fragments.SearchFragment
import com.example.proyectofinalmovil.fragments.SettingsFragment
import com.example.proyectofinalmovil.fragments.TeamFragment
import com.example.proyectofinalmovil.fragments.TournamentFragment
import com.example.proyectofinalmovil.provider.ApiClient
import com.example.proyectofinalmovil.viewmodel.MainMenuViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainMenuActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainMenuBinding

    val fragmentManager = supportFragmentManager

    private val mainMenuVM: MainMenuViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        Log.d("Session player", Session.sessionPlayer.toString())
        Session.changeLoadedPlayer(Session.sessionPlayer)
        //replaceFragments(PlayerFragment())

        showSelected()
        setListeners()

        /*CoroutineScope(Dispatchers.Main).launch {
            Log.d("PruebaAAAAA", ApiClient.apiClient.getPlayersById("swing").toString())
        }*/
    }

    private fun showSelected(){
        var selectedFragment = fragmentManager.findFragmentById(binding.fragmentContainer.id)

        if(selectedFragment is SearchFragment){
            binding.bottomNavigation.selectedItemId = R.id.item_search
        }
        if(selectedFragment is CreateFragment){
            binding.bottomNavigation.selectedItemId = R.id.item_create
        }
        if(selectedFragment is PlayerFragment){
            binding.bottomNavigation.selectedItemId = R.id.item_profile
        }
        if(selectedFragment is SettingsFragment){
            binding.bottomNavigation.selectedItemId = R.id.item_settings
        }
    }

    private fun setListeners(){
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.item_search -> {
                    replaceFragments(SearchFragment())
                    true
                }
                R.id.item_create -> {
                    replaceFragments(CreateFragment())
                    true
                }
                R.id.item_profile -> {
                    Session.changeLoadedPlayer(Session.sessionPlayer)

                    replaceFragments(PlayerFragment())
                    true
                }
                R.id.item_settings -> {
                    replaceFragments(SettingsFragment())
                    true
                }
                else -> false
            }
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                var selectedFragment = fragmentManager.findFragmentById(binding.fragmentContainer.id)

                if(selectedFragment is TeamFragment){
                    replaceFragments(SearchFragment())
                }
                if(selectedFragment is TournamentFragment){
                    replaceFragments(SearchFragment())
                }
                if(selectedFragment is PlayerFragment){
                    if(Session.loadedPlayer!!.id == Session.sessionPlayer.id){
                        finishAffinity()
                    }else{
                        replaceFragments(SearchFragment())
                    }
                }
                if(selectedFragment is SearchFragment){
                    Session.changeLoadedPlayer(Session.sessionPlayer)

                    replaceFragments(PlayerFragment())
                }
                if(selectedFragment is CreateFragment){
                    Session.changeLoadedPlayer(Session.sessionPlayer)

                    replaceFragments(PlayerFragment())
                }
                if(selectedFragment is SettingsFragment){
                    if(Session.homeSettings){
                        Session.changeLoadedPlayer(Session.sessionPlayer)

                        replaceFragments(PlayerFragment())
                    }else{
                        Session.homeSettings = true
                    }
                }
            }
        })
    }

    fun replaceFragments(fragment: Fragment){
        fragmentManager.beginTransaction().replace(binding.fragmentContainer.id, fragment).commit()
    }
}