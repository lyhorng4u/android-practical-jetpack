// File: app/src/main/java/com/lyhorng/sampleapp/data/repository/AuthRepository.kt
package com.lyhorng.sampleapp.data.repository

import com.lyhorng.sampleapp.data.model.request.ChangePasswordRequest
import com.lyhorng.sampleapp.data.model.request.LoginRequest
import com.lyhorng.sampleapp.data.model.request.RegisterRequest
import com.lyhorng.sampleapp.data.model.request.UpdateProfileRequest
import com.lyhorng.sampleapp.data.remote.ApiResult
import com.lyhorng.sampleapp.data.remote.BaseService
import com.lyhorng.sampleapp.data.model.response.AuthResponse
import com.lyhorng.sampleapp.data.model.response.UserResponse

class AuthRepository {

    private val apiService = BaseService.apiService

    suspend fun register(fullName: String, email: String, password: String): ApiResult<AuthResponse> {
        val request = RegisterRequest(fullName, email, password)
        return BaseService.safeApiCall {
            apiService.register(request)
        }
    }

    suspend fun login(email: String, password: String): ApiResult<AuthResponse> {
        val request = LoginRequest(email, password)
        return BaseService.safeApiCall {
            apiService.login(request)
        }
    }

    suspend fun getCurrentUser(): ApiResult<UserResponse> {
        return BaseService.safeApiCall {
            apiService.getCurrentUser()
        }
    }

    suspend fun getProfile(): ApiResult<UserResponse> {
        return BaseService.safeApiCall {
            apiService.getProfile()
        }
    }

    suspend fun updateProfile(fullName: String, email: String): ApiResult<UserResponse> {
        val request = UpdateProfileRequest(fullName, email)
        return BaseService.safeApiCall {
            apiService.updateProfile(request)
        }
    }

    suspend fun changePassword(currentPassword: String, newPassword: String): ApiResult<AuthResponse> {
        val request = ChangePasswordRequest(currentPassword, newPassword)
        return BaseService.safeApiCall {
            apiService.changePassword(request)
        }
    }
}