package com.deadlinehunters.pokedex.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.widget.FrameLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.preference.PreferenceManager
import com.deadlinehunters.pokedex.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

        PreferenceManager.setDefaultValues(this, R.xml.preferences, false)
        val trainerName = PreferenceManager.getDefaultSharedPreferences(this).getString(
            "trainername_preference",
            resources.getString(R.string.username_preference_default_value)
        )

        findViewById<BottomNavigationView>(R.id.bottomNavigationView).setupWithNavController(
            findNavController(
                R.id.nav_host
            )
        )

        val snackBar = Snackbar.make(findViewById(android.R.id.content),"Welcome, $trainerName", Snackbar.LENGTH_SHORT)
        val snackView = snackBar.view
        val params = snackView.layoutParams as FrameLayout.LayoutParams
        params.gravity = Gravity.TOP
        snackView.layoutParams = params
        snackBar.show()
    }
}