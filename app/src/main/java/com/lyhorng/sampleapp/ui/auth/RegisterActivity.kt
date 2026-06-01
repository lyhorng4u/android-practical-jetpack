// File: app/src/main/java/com/lyhorng/sampleapp/ui/auth/RegisterActivity.kt
package com.lyhorng.sampleapp.ui.auth

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.lyhorng.sampleapp.databinding.ActivityRegisterBinding
import com.lyhorng.sampleapp.ui.main.MainActivity
import com.lyhorng.sampleapp.viewmodel.AuthViewModel
import com.lyhorng.sampleapp.viewmodel.utils.PreferenceManager
import com.lyhorng.sampleapp.viewmodel.utils.Resource
import com.lyhorng.sampleapp.viewmodel.utils.showToast

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val viewModel: AuthViewModel by viewModels()
    private lateinit var preferenceManager: PreferenceManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        preferenceManager = PreferenceManager(this)

        setupClickListeners()
        observeViewModel()
    }

    private fun setupClickListeners() {
        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.btnRegister.setOnClickListener {
            validateAndRegister()
        }

        binding.tvLogin.setOnClickListener {
            finish()
        }
    }

    private fun validateAndRegister() {
        val fullName = binding.etFullName.text.toString().trim()
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString()
        val confirmPassword = binding.etConfirmPassword.text.toString()

        // Reset errors
        binding.tilFullName.error = null
        binding.tilEmail.error = null
        binding.tilPassword.error = null
        binding.tilConfirmPassword.error = null

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
            password.isEmpty() -> {
                binding.tilPassword.error = "Password is required"
                return
            }
            password.length < 6 -> {
                binding.tilPassword.error = "Password must be at least 6 characters"
                return
            }
            confirmPassword.isEmpty() -> {
                binding.tilConfirmPassword.error = "Please confirm your password"
                return
            }
            password != confirmPassword -> {
                binding.tilConfirmPassword.error = "Passwords do not match"
                return
            }
        }

        // Call API
        viewModel.register(fullName, email, password)
    }

    private fun observeViewModel() {
        viewModel.registerResult.observe(this) { resource ->
            when (resource) {

                is Resource.Loading -> {
                    showLoading(true)
                }

                is Resource.Success -> {
                    showLoading(false)

                    val response = resource.data
                    val authData = response?.data   // ✅ FIX

                    if (response?.success == true && authData != null) {

                        // 1. Save token
                        preferenceManager.saveToken(authData.token)

                        // 2. Save user
                        val user = authData.user
                        preferenceManager.saveUserData(
                            user.id,
                            user.email,
                            user.fullName
                        )

                        showToast(response.message)
                        navigateToMain()

                    } else {
                        showToast(response?.message ?: "Registration failed")
                    }
                }

                is Resource.Error -> {
                    showLoading(false)
                    showToast(resource.message ?: "Registration failed")
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) android.view.View.VISIBLE else android.view.View.GONE
        binding.btnRegister.isEnabled = !isLoading
        binding.etFullName.isEnabled = !isLoading
        binding.etEmail.isEnabled = !isLoading
        binding.etPassword.isEnabled = !isLoading
        binding.etConfirmPassword.isEnabled = !isLoading
    }

    private fun navigateToMain() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}