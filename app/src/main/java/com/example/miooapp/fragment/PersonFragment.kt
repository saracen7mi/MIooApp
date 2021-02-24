package com.example.miooapp.fragment
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.example.miooapp.R
import kotlinx.android.synthetic.main.fragment_person.view.*


class PersonFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val  view=inflater.inflate(R.layout.fragment_person, container, false)
        view.five.setOnClickListener {
            val intent = Intent(context, FiristActivity::class.java)
            startActivity(intent)}
        view.person_two.setOnClickListener {   Navigation.findNavController(view).navigate(R.id.action_personFragment_to_biciklFragment) }
        view.person_one.setOnClickListener {   Navigation.findNavController(view).navigate(R.id.action_personFragment_to_selectedBiciklFragment) }
        return view
    }

}