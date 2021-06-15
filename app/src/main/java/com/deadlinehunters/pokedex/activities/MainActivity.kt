package com.deadlinehunters.pokedex.activities

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.preference.PreferenceManager
import com.deadlinehunters.pokedex.R

class MainActivity : AppCompatActivity() {
    private val splashTimeOut: Long = 4000

    @SuppressLint("InflateParams")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val view =
            (getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater).inflate(R.layout.activity_main,
                null)
        supportActionBar?.hide()

        /* initialize stuff here */
        initializePreferenceManager()
        initializeSplashScreen(view)

        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this@MainActivity, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }, splashTimeOut)
    }

    private fun initializePreferenceManager() {
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false)
    }

    @SuppressLint("SetTextI18n")
    private fun initializeSplashScreen(view: View) {
        val trainerName = PreferenceManager.getDefaultSharedPreferences(this).getString(
            "trainername_preference",
            resources.getString(R.string.username_preference_default_value)
        )
        val welcomeMessageTextView =
            view.findViewById<TextView>(R.id.splash_welcome_message_textview)

        if (trainerName.isNullOrEmpty()) {
            welcomeMessageTextView.text = "Trainer!"
        } else {
            welcomeMessageTextView.text = "Trainer $trainerName!"
        }
        setContentView(view)
    }
}