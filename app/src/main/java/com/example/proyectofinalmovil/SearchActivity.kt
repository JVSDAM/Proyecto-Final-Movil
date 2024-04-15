package com.example.proyectofinalmovil

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.SearchView
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.proyectofinalmovil.adapters.PlayerAdapter
import com.example.proyectofinalmovil.databinding.ActivitySearchBinding
import com.example.proyectofinalmovil.models.Player
import com.example.proyectofinalmovil.viewmodel.PlayersViewModel


class SearchActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySearchBinding
    private val playersVM : PlayersViewModel by viewModels()
    val playerAdapter = PlayerAdapter(mutableListOf(), {player -> showInformation(player)})

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setRecycler()
        setListeners()

        playersVM.playerList.observe(this){
            playerAdapter.list = it
            playerAdapter.notifyDataSetChanged()
        }
    }

    private fun setRecycler(){
        val layoutManager = LinearLayoutManager(this)
        binding.playerRecycler.layoutManager = layoutManager
        binding.playerRecycler.adapter = playerAdapter
    }

    private fun setListeners(){
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                playersVM.searchPlayers(binding.searchView.query.toString().lowercase().replace(" ", "_"))
                return true
            }
        })
    }

    private fun showInformation(player: Player){
        Log.d("Enviando datos", "a PLAYER PROFILE de " + player.name)
        val i = Intent(this, PlayerActivity::class.java).apply{
            putExtra("PLAYER", player)
        }

        startActivity(i)
    }
}