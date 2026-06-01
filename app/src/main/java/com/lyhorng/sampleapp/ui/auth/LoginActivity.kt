package com.lyhorng.sampleapp.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.lyhorng.sampleapp.databinding.ActivityLoginBinding
import com.lyhorng.sampleapp.ui.main.MainActivity
import com.lyhorng.sampleapp.viewmodel.AuthViewModel
import com.lyhorng.sampleapp.viewmodel.utils.PreferenceManager
import com.lyhorng.sampleapp.viewmodel.utils.Resource

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val viewModel: AuthViewModel by viewModels()
    private lateinit var preferenceManager: PreferenceManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        preferenceManager = PreferenceManager(this)

        if (preferenceManager.isLoginIn()) {
            navigateToMain()
            return
        }

        setupClickListeners()
        observeViewModel()
    }

    private fun setupClickListeners() {
        binding.btnLogin.setOnClickListener {
            validateAndLogin()
        }
    }

    private fun validateAndLogin() {
        val email = binding.inputEmail.getText()
        val password = binding.inputPassword.getText()

        binding.inputEmail.clearError()
        binding.inputPassword.clearError()

        when {
            email.isEmpty() -> {
                binding.inputEmail.setError("Email is required")
                binding.inputEmail.focus()
                return
            }

            !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                binding.inputEmail.setError("Invalid email format")
                binding.inputEmail.focus()
                return
            }

            password.isEmpty() -> {
                binding.inputPassword.setError("Password is required")
                binding.inputPassword.focus()
                return
            }

            password.length < 6 -> {
                binding.inputPassword.setError("Password must be at least 6 characters")
                binding.inputPassword.focus()
                return
            }
        }

        viewModel.login(email, password)
    }

    private fun observeViewModel() {
        viewModel.loginResult.observe(this) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    showLoading(true)
                }

                is Resource.Success -> {
                    showLoading(false)

                    val response = resource.data
                    val authData = response?.data

                    if (response?.success == true && !authData?.token.isNullOrEmpty()) {
                        preferenceManager.saveToken(authData!!.token)

                        authData.user.let { user ->
                            preferenceManager.saveUserData(
                                user.id,
                                user.email,
                                user.fullName
                            )
                        }

                        showToast(response.message ?: "Login successful")
                        navigateToMain()
                    } else {
                        showToast(response?.message ?: "Login failed")
                    }
                }

                is Resource.Error -> {
                    showLoading(false)
                    showToast(resource.message ?: "Login failed")
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.btnLogin.isEnabled = !isLoading
        binding.inputEmail.setInputEnabled(!isLoading)
        binding.inputPassword.setInputEnabled(!isLoading)
    }

    private fun navigateToMain() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}