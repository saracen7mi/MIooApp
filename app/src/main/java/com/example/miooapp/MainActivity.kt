package com.example.miooapp



import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.fragment_firist.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        val bottomNavigationView=findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        val navControl=findNavController(R.id.fragment)
val appBar= AppBarConfiguration(setOf(R.id.bookFragment,R.id.biciklFragment,R.id.messageFragment,R.id.personFragment))
        bottomNavigationView.setupWithNavController(navControl)
    }

}