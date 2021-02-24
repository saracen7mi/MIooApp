package com.example.miooapp.fragment

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.miooapp.R
import com.example.miooapp.activity.LoginActivity
import com.example.miooapp.activity.RegisterActivity
import kotlinx.android.synthetic.main.fragment_firist.*


class FiristActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_firist)


        create_acount_login_button.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)}


        create_acount_create_button.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)}

    }

}