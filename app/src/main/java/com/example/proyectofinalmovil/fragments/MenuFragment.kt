package com.example.proyectofinalmovil.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.proyectofinalmovil.CreateActivity
import com.example.proyectofinalmovil.PlayerActivity
import com.example.proyectofinalmovil.R
import com.example.proyectofinalmovil.SearchActivity
import com.example.proyectofinalmovil.SettingsActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

/**
 * A simple [Fragment] subclass.
 * Use the [MenuFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MenuFragment : Fragment() {
    lateinit var navigationBar:BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navigationBar = view.findViewById<BottomNavigationView>(R.id.bottom_navigation)

        mostrarSombra()

        setListeners()
    }

    private fun mostrarSombra(){

        if(this.activity is SearchActivity){
            navigationBar.selectedItemId = R.id.item_search
        }
        if(this.activity is CreateActivity){
            navigationBar.selectedItemId = R.id.item_create
        }
        if(this.activity is PlayerActivity){
            navigationBar.selectedItemId = R.id.item_profile
        }
        if(this.activity is SettingsActivity){
            navigationBar.selectedItemId = R.id.item_settings
        }
    }

    private fun setListeners(){
        navigationBar.setOnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.item_search -> {
                    startActivity(Intent(requireContext(), SearchActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION))
                    true
                }
                R.id.item_create -> {
                    startActivity(Intent(requireContext(), CreateActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION))
                    true
                }
                R.id.item_profile -> {
                    startActivity(Intent(requireContext(), PlayerActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION))
                    true
                }
                R.id.item_settings -> {
                    startActivity(Intent(requireContext(), SettingsActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION))
                    true
                }
                else -> false
            }
        }
    }
}