package com.example.miooapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.example.miooapp.R
import kotlinx.android.synthetic.main.fragment_book.view.*


class BookFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =inflater.inflate(R.layout.fragment_book, container, false)
   //   findNavController(BookFragment()).navigate(R.id.action_bookFragment_to_biciklFragment)
        view.book_botom_sek.setOnClickListener { Navigation.findNavController(view).navigate(R.id.action_bookFragment_to_selectedBiciklFragment) }
        view.book_map_botom.setOnClickListener { Navigation.findNavController(view).navigate(R.id.action_bookFragment_to_mapFragment) }

        return view
    }

}