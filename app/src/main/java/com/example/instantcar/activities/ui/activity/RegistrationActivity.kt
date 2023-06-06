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
import com.example.instantcar.activities.utilities.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RegistrationActivity : AppCompatActivity() {
    private lateinit var registerButton: Button
    private lateinit var firstnameEditText: EditText
    private lateinit var lastnameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var backToLoginTextView: TextView

    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        registerButton = findViewById(R.id.registerRegistrationButton)
        firstnameEditText = findViewById(R.id.firstnameRegistrationEditText)
        lastnameEditText = findViewById(R.id.lastnameRegistrationEditText)
        emailEditText = findViewById(R.id.emailRegistrationEditText)
        passwordEditText = findViewById(R.id.passwordRegistrationEditText)
        backToLoginTextView = findViewById(R.id.backToLoginRegistrationTextView)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        databaseReference = database.reference.child(Constants.USER_PROFILE_DATABASE_PATH_UPLOADS)

        register()
    }

    private fun register() {
        registerButton.setOnClickListener {
            when {
                TextUtils.isEmpty(firstnameEditText.text.toString()) -> {
                    firstnameEditText.error = "Please enter first name"
                    return@setOnClickListener
                }
                TextUtils.isEmpty(lastnameEditText.text.toString()) -> {
                    lastnameEditText.error = "Please enter last name"
                    return@setOnClickListener
                }
                TextUtils.isEmpty(emailEditText.text.toString()) -> {
                    emailEditText.error = "Please enter email"
                    return@setOnClickListener
                }
                TextUtils.isEmpty(passwordEditText.text.toString()) -> {
                    passwordEditText.error =
                        "Please enter password" //use property access syntax instead of setter method
                    return@setOnClickListener
                }
                else -> auth.createUserWithEmailAndPassword(
                    emailEditText.text.toString(),
                    passwordEditText.text.toString()
                )
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            //store user data in realtime database
                            val currentUser = auth.currentUser
                            val currentUSerDb = databaseReference.child((currentUser?.uid!!))
                            currentUSerDb.child("firstname")
                                .setValue(firstnameEditText.text.toString())
                            currentUSerDb.child("lastname")
                                .setValue(lastnameEditText.text.toString())
                            currentUSerDb.child("email")
                                .setValue(emailEditText.text.toString())

                            Toast.makeText(
                                this@RegistrationActivity,
                                "Registered Successfully.",
                                Toast.LENGTH_LONG
                            ).show()
                            finish()

                        } else {
                            Toast.makeText(
                                this@RegistrationActivity,
                                "Registration failed, please try again!",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
            }
        }

        backToLoginTextView.setOnClickListener {
            startActivity(Intent(this@RegistrationActivity, LoginActivity::class.java))
        }
    }
}