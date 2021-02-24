package com.example.miooapp.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.miooapp.R
import kotlinx.android.synthetic.main.fragment_bicikl.view.*


class BiciklFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val  view=inflater.inflate(R.layout.fragment_bicikl, container, false)
     
        view.mybike_button.setOnClickListener { Navigation.findNavController(view).navigate(R.id.action_biciklFragment_to_selectedBiciklFragment) }
        return view
    }


}