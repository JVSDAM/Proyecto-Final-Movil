package com.example.proyectofinalmovil.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.cardview.widget.CardView
import com.example.proyectofinalmovil.CreateTournamentActivity
import com.example.proyectofinalmovil.PlayerActivity
import com.example.proyectofinalmovil.R
import com.example.proyectofinalmovil.SearchActivity
import com.example.proyectofinalmovil.TournamentActivity
import com.example.proyectofinalmovil.models.Player

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MenuFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MenuFragment : Fragment() {
    lateinit var btnSearch: ImageButton
    lateinit var btnProfile: ImageButton
    lateinit var btnCreateTournament: ImageButton
    lateinit var cvSearch: CardView
    lateinit var cvProfile: CardView
    lateinit var cvCreateTournament: CardView

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
        btnSearch = view.findViewById<ImageButton>(R.id.btnSearch)
        btnProfile = view.findViewById<ImageButton>(R.id.btnProfile)
        btnCreateTournament = view.findViewById<ImageButton>(R.id.btnCreateTournament)
        cvSearch = view.findViewById<CardView>(R.id.cvSearch)
        cvProfile = view.findViewById<CardView>(R.id.cvProfile)
        cvCreateTournament = view.findViewById<CardView>(R.id.cvCreateTournament)

        mostrarSombra()

        setListeners()
    }

    private fun mostrarSombra(){
        cvSearch.cardElevation = 0f
        cvProfile.cardElevation = 0f
        cvCreateTournament.cardElevation = 0f

        if(this.activity is SearchActivity){
            cvSearch.cardElevation = 10f
        }
        if(this.activity is PlayerActivity){
            cvProfile.cardElevation = 10f
        }
        if(this.activity is CreateTournamentActivity){
            cvCreateTournament.cardElevation = 10f
        }
    }

    private fun setListeners(){
        btnSearch.setOnClickListener(){
            cvSearch.cardElevation = 10f
            cvProfile.cardElevation = 0f
            cvCreateTournament.cardElevation = 0f
            startActivity(Intent(requireContext(), SearchActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION))
        }
        btnProfile.setOnClickListener(){
            cvSearch.cardElevation = 0f
            cvProfile.cardElevation = 10f
            cvCreateTournament.cardElevation = 0f
            startActivity(Intent(requireContext(), PlayerActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION))
        }
        btnCreateTournament.setOnClickListener(){
            cvSearch.cardElevation = 0f
            cvProfile.cardElevation = 0f
            cvCreateTournament.cardElevation = 10f

            startActivity(Intent(requireContext(), TournamentActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION))
        }
    }
}