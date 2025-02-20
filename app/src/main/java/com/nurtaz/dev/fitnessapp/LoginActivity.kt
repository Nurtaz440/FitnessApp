package com.nurtaz.dev.fitnessapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.nurtaz.dev.fitnessapp.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginButton.setOnClickListener{
            val loginUsername = binding.loginUsername.text.toString()
            val loginPassword = binding.loginPassword.text.toString()

            if(loginUsername.isNotEmpty() && loginPassword.isNotEmpty()){
                loginUser(loginUsername, loginPassword)
            }
            else{
                Toast.makeText(this@LoginActivity, "Please Fill Out All Fields!", Toast.LENGTH_SHORT).show()
            }
        }

        binding.signupRedirect.setOnClickListener{
            startActivity(Intent(this@LoginActivity, SignupActivity::class.java))
            finish()
        }

        supportActionBar?.hide()
    }

    private fun loginUser(username: String, password: String) {
        val usersRef = db.collection("users")
        val query = usersRef.whereEqualTo("username", username)



        query.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                for (documentSnapshot in task.result!!) {
                    val usernameSnapshot = documentSnapshot.getString("username")
                    val passwordSnapshot = documentSnapshot.getString("password")

                    if (usernameSnapshot != null && passwordSnapshot == password) {
                        Toast.makeText(this@LoginActivity, "Login Successful!", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        intent.putExtra("username", usernameSnapshot)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this@LoginActivity, "Incorrect Password", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            if (task.result!!.size() == 0) {
                Toast.makeText(this@LoginActivity, "User doesn't exist", Toast.LENGTH_SHORT).show()
            }
        }
    }
}