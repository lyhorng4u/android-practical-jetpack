package com.lyhorng.sampleapp.data.model.request

data class ChangePasswordRequest (
    val currentPassword: String,
    val newPassword: String
)
