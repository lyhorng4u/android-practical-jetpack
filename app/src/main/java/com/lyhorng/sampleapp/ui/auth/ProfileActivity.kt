// File: app/src/main/java/com/lyhorng/sampleapp/ui/auth/ProfileActivity.kt
package com.lyhorng.sampleapp.ui.auth

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.lyhorng.sampleapp.databinding.ActivityProfileBinding
import com.lyhorng.sampleapp.viewmodel.AuthViewModel
import com.lyhorng.sampleapp.viewmodel.utils.PreferenceManager
import com.lyhorng.sampleapp.viewmodel.utils.Resource
import com.lyhorng.sampleapp.viewmodel.utils.showToast

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    private val viewModel: AuthViewModel by viewModels()
    private lateinit var preferenceManager: PreferenceManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        preferenceManager = PreferenceManager(this)

        setupToolbar()
        setupClickListeners()
        observeViewModel()
        loadProfile()
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    private fun setupClickListeners() {
        binding.btnUpdate.setOnClickListener {
            validateAndUpdate()
        }

        binding.btnChangePassword.setOnClickListener {
            startActivity(Intent(this, ChangePasswordActivity::class.java))
        }

        binding.btnLogout.setOnClickListener {
            showLogoutDialog()
        }
    }

    private fun loadProfile() {
        viewModel.getProfile()
    }

    private fun validateAndUpdate() {
        val fullName = binding.etFullName.text.toString().trim()
        val email = binding.etEmail.text.toString().trim()

        // Reset errors
        binding.tilFullName.error = null
        binding.tilEmail.error = null

        // Validation
        when {
            fullName.isEmpty() -> {
                binding.tilFullName.error = "Full name is required"
                return
            }
            fullName.length < 3 -> {
                binding.tilFullName.error = "Name must be at least 3 characters"
                return
            }
            email.isEmpty() -> {
                binding.tilEmail.error = "Email is required"
                return
            }
            !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                binding.tilEmail.error = "Invalid email format"
                return
            }
        }

        viewModel.updateProfile(fullName, email)
    }

    private fun observeViewModel() {
        // Profile loading
        viewModel.profileResult.observe(this) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    showLoading(true)
                }
                is Resource.Success -> {
                    showLoading(false)
                    resource.data?.let { user ->
                        binding.etFullName.setText(user.fullName)
                        binding.etEmail.setText(user.email)
                    }
                }
                is Resource.Error -> {
                    showLoading(false)
                    showToast(resource.message ?: "Failed to load profile")
                }
            }
        }

        // Profile update
        viewModel.updateProfileResult.observe(this) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    showLoading(true)
                }
                is Resource.Success -> {
                    showLoading(false)
                    resource.data?.let { user ->
                        // Update saved preferences
                        preferenceManager.saveUserData(user.id, user.email, user.fullName)
                        showToast("Profile updated successfully")
                    }
                }
                is Resource.Error -> {
                    showLoading(false)
                    showToast(resource.message ?: "Failed to update profile")
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) android.view.View.VISIBLE else android.view.View.GONE
        binding.btnUpdate.isEnabled = !isLoading
    }

    private fun showLogoutDialog() {
        AlertDialog.Builder(this)
            .setTitle("Logout")
            .setMessage("Are you sure you want to logout?")
            .setPositiveButton("Yes") { _, _ ->
                logout()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun logout() {
        preferenceManager.clearAll()
        startActivity(Intent(this, LoginActivity::class.java))
        finishAffinity()
    }
}
