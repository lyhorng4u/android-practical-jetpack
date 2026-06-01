package com.lyhorng.sampleapp.ui.auth

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.lyhorng.sampleapp.databinding.ActivityChangePasswordBinding
import com.lyhorng.sampleapp.viewmodel.AuthViewModel
import com.lyhorng.sampleapp.viewmodel.utils.Resource
import com.lyhorng.sampleapp.viewmodel.utils.showToast

class ChangePasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChangePasswordBinding
    private val viewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupClickListeners()
        observeViewModel()
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    private fun setupClickListeners() {
        binding.btnChangePassword.setOnClickListener {
            validateAndChangePassword()
        }
    }

    private fun validateAndChangePassword() {
        val currentPassword = binding.etCurrentPassword.text.toString()
        val newPassword = binding.etNewPassword.text.toString()
        val confirmPassword = binding.etConfirmPassword.text.toString()

        // Reset errors
        binding.tilCurrentPassword.error = null
        binding.tilNewPassword.error = null
        binding.tilConfirmPassword.error = null

        // Validation
        when {
            currentPassword.isEmpty() -> {
                binding.tilCurrentPassword.error = "Current password is required"
                return
            }
            newPassword.isEmpty() -> {
                binding.tilNewPassword.error = "New password is required"
                return
            }
            newPassword.length < 6 -> {
                binding.tilNewPassword.error = "Password must be at least 6 characters"
                return
            }
            confirmPassword.isEmpty() -> {
                binding.tilConfirmPassword.error = "Please confirm your new password"
                return
            }
            newPassword != confirmPassword -> {
                binding.tilConfirmPassword.error = "Passwords do not match"
                return
            }
            currentPassword == newPassword -> {
                binding.tilNewPassword.error = "New password must be different from current password"
                return
            }
        }

        viewModel.changePassword(currentPassword, newPassword)
    }

    private fun observeViewModel() {
        viewModel.changePasswordResult.observe(this) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    showLoading(true)
                }
                is Resource.Success -> {
                    showLoading(false)
                    showToast(resource.data ?: "Password changed successfully")
                    finish()
                }
                is Resource.Error -> {
                    showLoading(false)
                    showToast(resource.message ?: "Failed to change password")
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) android.view.View.VISIBLE else android.view.View.GONE
        binding.btnChangePassword.isEnabled = !isLoading
    }
}