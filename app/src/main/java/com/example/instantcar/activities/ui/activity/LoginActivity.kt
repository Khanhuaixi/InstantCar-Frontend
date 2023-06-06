package com.example.instantcar.activities.ui.activity

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.instantcar.R
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    private lateinit var loginButton: Button
    private lateinit var registerDescriptionTextView: TextView
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        loginButton = findViewById(R.id.loginLoginButton)
        registerDescriptionTextView = findViewById(R.id.registerDescriptionLoginTextView)
        emailEditText = findViewById(R.id.emailLoginEditText)
        passwordEditText = findViewById(R.id.passwordLoginEditText)

        auth = FirebaseAuth.getInstance()

        //check if user already logged in
        val currentUser = auth.currentUser
        //if already logged in then start main activity, no need login again
        if (currentUser != null) {
            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
            //finish login activity
            finish()
        } else {
            login()
            registerDescriptionTextView.setOnClickListener {
                startActivity(Intent(this@LoginActivity, RegistrationActivity::class.java))
            }
        }
    }

    //allow user login to app
    private fun login() {
        loginButton.setOnClickListener {
            if (TextUtils.isEmpty(emailEditText.text.toString())) {
                emailEditText.error = "Please enter email"
                return@setOnClickListener
            } else if (TextUtils.isEmpty(passwordEditText.text.toString())) {
                passwordEditText.error = "Please enter password"
                return@setOnClickListener
            }
            auth.signInWithEmailAndPassword(
                emailEditText.text.toString(),
                passwordEditText.text.toString()
            )
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        //if login successful, go to main activity
                        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                        //finish current login activity, since no use anymore
                        finish()
                    } else {
                        //if login fail, show error message in toast
                        Toast.makeText(
                            this@LoginActivity,
                            "Login failed, please try again! ",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
        }
    }
}