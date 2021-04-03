package com.deadlinehunters.pokedex.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.preference.PreferenceManager
import com.deadlinehunters.pokedex.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

        PreferenceManager.setDefaultValues(this, R.xml.preferences, false)

        findViewById<BottomNavigationView>(R.id.bottomNavigationView).setupWithNavController(findNavController(
            R.id.nav_host
        ))

    }
}