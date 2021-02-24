package com.example.miooapp.activity


import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.miooapp.fragment.BookFragment
import com.example.miooapp.MainActivity
import com.example.miooapp.R
import com.example.miooapp.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_registar.*

class RegisterActivity : AppCompatActivity() {
    private val TAG = "RegisterActivity"
    private val REQUEST_CODE = 0
    private var loading = false
    val fragement=fragment

    private lateinit var auth: FirebaseAuth
    private lateinit var storage: FirebaseStorage
    private lateinit var database: FirebaseDatabase



    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registar)


        auth = FirebaseAuth.getInstance()
        storage = FirebaseStorage.getInstance()
        database = FirebaseDatabase.getInstance()



        register_button.setOnClickListener {
            createNewUser()
            saveUserToFirebaseDatabase()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()



        }

        already_have_account_text_view.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }




    private fun createNewUser() {
        val email = email_edittext_register.text.toString()
        val password = password_edittext_register.text.toString()


        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter your email and password", Toast.LENGTH_LONG).show()
            return
        }

        Log.d(TAG, "\nUsername: $email Passowrd: $password")

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                val user = auth.currentUser
                Log.d(TAG, "createUserWithEmail:success")
                Log.d(TAG, user.toString())

            } else {
                Log.d(TAG, "createUserWithEmail:failure", task.exception)
                Toast.makeText(this, "Authentication failed: ${task.exception}", Toast.LENGTH_LONG).show()
            }
        }
            .addOnFailureListener(this) {
                Log.d(TAG, "Failed to create user: ${it.message}")
            }
    }



    private fun saveUserToFirebaseDatabase() {
        val uid = auth.uid ?: ""
        val ref = database.getReference("/users/$uid")

        val user = User(uid,first_edittext_register.text.toString())

        ref.setValue(user).addOnSuccessListener {
            Log.d(TAG, "User saved to database  ")

            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
            .addOnFailureListener{
                Toast.makeText(this, "User details mot saved: ${it.message}", Toast.LENGTH_LONG).show()
            }
    }


}