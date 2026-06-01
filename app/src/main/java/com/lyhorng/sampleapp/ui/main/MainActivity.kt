package com.lyhorng.sampleapp.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.lyhorng.sampleapp.R
import com.lyhorng.sampleapp.databinding.ActivityMainBinding
import com.lyhorng.sampleapp.ui.auth.LoginActivity
import com.lyhorng.sampleapp.ui.auth.ProfileActivity
import com.lyhorng.sampleapp.viewmodel.utils.PreferenceManager

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var preferenceManager: PreferenceManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        preferenceManager = PreferenceManager(this)

        // Check if logged in
//        if (!preferenceManager.isLoginIn()) {
//            navigateToLogin()
//            return
//        }

        setupToolbar()
        displayWelcomeMessage()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = "Cambonex"
    }

    private fun displayWelcomeMessage() {
        val userName = preferenceManager.getUserName() ?: "User"
        binding.tvWelcome.text = "Welcome back, $userName!"
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_profile -> {
                startActivity(Intent(this, ProfileActivity::class.java))
                true
            }
            R.id.action_logout -> {
                logout()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun logout() {
        preferenceManager.clearAll()
        navigateToLogin()
    }

    private fun navigateToLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}