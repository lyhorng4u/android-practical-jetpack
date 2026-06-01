package com.lyhorng.sampleapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lyhorng.sampleapp.data.model.response.AuthResponse
import com.lyhorng.sampleapp.data.model.response.User
import com.lyhorng.sampleapp.data.remote.ApiResult
import com.lyhorng.sampleapp.data.repository.AuthRepository
import com.lyhorng.sampleapp.viewmodel.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.log

class AuthViewModel : ViewModel() {

    private val repository = AuthRepository()
    private val _registerResult = MutableLiveData<Resource<AuthResponse>>()
    val registerResult: LiveData<Resource<AuthResponse>> = _registerResult

    private val _loginResult = MutableLiveData<Resource<AuthResponse>>()
    val loginResult: LiveData<Resource<AuthResponse>> = _loginResult

    private val _profileResult = MutableLiveData<Resource<User>>()
    val profileResult: LiveData<Resource<User>> = _profileResult

    private val _updateProfileResult = MutableLiveData<Resource<User>>()
    val updateProfileResult: LiveData<Resource<User>> = _updateProfileResult

    private val _changePasswordResult = MutableLiveData<Resource<String>>()
    val changePasswordResult: LiveData<Resource<String>> = _changePasswordResult

    fun register(fullName: String, email: String, password: String) {
        viewModelScope.launch {
            _registerResult.value = Resource.Loading()
            try {
                when (val result = repository.register(fullName, email, password)) {
                    is ApiResult.Success -> {
                        _registerResult.value = Resource.Success(result.data)
                    }
                    is ApiResult.Error -> {
                        _registerResult.value = Resource.Error(result.message)
                    }
                }
            } catch (e: Exception) {
                _registerResult.value = Resource.Error(e.message ?: "Something went wrong")
            }
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _loginResult.value = Resource.Loading()
            try {
                val result = withContext(Dispatchers.IO) {
                    repository.login(email, password)
                }

                when (result) {
                    is ApiResult.Success -> {
                        Log.e("TAG", "login: $result")
                        _loginResult.value = Resource.Success(result.data)
                    }
                    is ApiResult.Error -> {
                        _loginResult.value = Resource.Error(result.message)
                    }
                }

            } catch (e: Exception) {
                _loginResult.value = Resource.Error(e.message ?: "Something went wrong")
            }
        }
    }
    fun getProfile() {
        viewModelScope.launch {
            _profileResult.value = Resource.Loading()
            try {
                when (val result = repository.getProfile()) {
                    is ApiResult.Success -> {
                        _profileResult.value = Resource.Success(result.data.user)
                    }
                    is ApiResult.Error -> {
                        _profileResult.value = Resource.Error(result.message)
                    }
                }
            } catch (e: Exception) {
                _profileResult.value = Resource.Error(e.message ?: "Something went wrong")
            }
        }
    }

    fun updateProfile(fullName: String, email: String) {
        viewModelScope.launch {
            _updateProfileResult.value = Resource.Loading()
            try {
                when (val result = repository.updateProfile(fullName, email)) {
                    is ApiResult.Success -> {
                        _updateProfileResult.value = Resource.Success(result.data.user)
                    }
                    is ApiResult.Error -> {
                        _updateProfileResult.value = Resource.Error(result.message)
                    }
                }
            } catch (e: Exception) {
                _updateProfileResult.value = Resource.Error(e.message ?: "Something went wrong")
            }
        }
    }

    fun changePassword(currentPassword: String, newPassword: String) {
        viewModelScope.launch {
            _changePasswordResult.value = Resource.Loading()
            try {
                when (val result = repository.changePassword(currentPassword, newPassword)) {
                    is ApiResult.Success -> {
                        _changePasswordResult.value = Resource.Success(result.data.message)
                    }
                    is ApiResult.Error -> {
                        _changePasswordResult.value = Resource.Error(result.message)
                    }
                }
            } catch (e: Exception) {
                _changePasswordResult.value = Resource.Error(e.message ?: "Something went wrong")
            }
        }
    }
}