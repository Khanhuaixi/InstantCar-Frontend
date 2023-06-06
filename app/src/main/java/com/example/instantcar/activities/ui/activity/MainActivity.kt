package com.example.instantcar.activities.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.instantcar.R
import com.example.instantcar.activities.ui.fragment.AccountFragment
import com.example.instantcar.activities.ui.fragment.BookingFragment
import com.example.instantcar.activities.ui.fragment.ExploreFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val exploreFragment = ExploreFragment()
    private val bookingFragment = BookingFragment()
    private val accountFragment = AccountFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //check if is from BookingSuccessfulActivity
        val paid: Boolean = intent.getBooleanExtra("paid", false)
        if (paid) {
            bottomNavigationView.selectedItemId = R.id.nav_booking
            replaceFragment(bookingFragment)
        } else {
            bottomNavigationView.selectedItemId = R.id.nav_explore
            replaceFragment(exploreFragment)
        }

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_explore -> replaceFragment(exploreFragment)
                R.id.nav_booking -> replaceFragment(bookingFragment)
                R.id.nav_account -> replaceFragment(accountFragment)
            }
            true
        }
    }

    private fun replaceFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.mainFragmentContainerFrameLayout, fragment)
        transaction.commit()
    }
}