package com.deadlinehunters.pokedex.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.widget.FrameLayout
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.preference.PreferenceManager
import com.deadlinehunters.pokedex.R
import com.deadlinehunters.pokedex.handlers.InternetConnectionHandler
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        supportActionBar?.hide()

        if (!InternetConnectionHandler(this).isOnline())
            Toast.makeText(applicationContext, "No internet connection found. Please check your connection and try again.", Toast.LENGTH_SHORT)
                .show()

        findViewById<BottomNavigationView>(R.id.bottomNavigationView).setupWithNavController(
            findNavController(
                R.id.nav_host
            )
        )
    }
}