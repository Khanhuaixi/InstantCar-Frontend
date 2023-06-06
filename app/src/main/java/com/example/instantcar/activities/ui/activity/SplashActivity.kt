package com.example.instantcar.activities.ui.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.example.instantcar.R
import com.google.firebase.auth.FirebaseAuth

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        Handler().postDelayed(
            {
                if (FirebaseAuth.getInstance().currentUser != null) {
                    Log.e("USER", FirebaseAuth.getInstance().currentUser.toString())
                    startActivity(Intent(this, MainActivity::class.java))
                } else {
                    startActivity(Intent(this, LoginActivity::class.java))
                }
                finish()
            }, 2000
        )

    }
}