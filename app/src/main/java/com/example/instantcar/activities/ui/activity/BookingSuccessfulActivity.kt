package com.example.instantcar.activities.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.instantcar.R
import kotlin.properties.Delegates

class BookingSuccessfulActivity : AppCompatActivity() {
    private lateinit var seeBookingsButton: Button
    private var paid by Delegates.notNull<Boolean>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking_successful)

        seeBookingsButton = findViewById(R.id.seeBookingsBookingSuccessfulButton)
        paid = true

        seeBookingsButton.setOnClickListener {
            this.let {
                val intent = Intent(it, MainActivity::class.java)
                intent.putExtra("paid", paid)
                it.startActivity(intent)
            }
            finish()
        }
    }
}