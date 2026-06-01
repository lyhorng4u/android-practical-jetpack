package com.lyhorng.sampleapp.data.model.response

data class User(
    val id: Int,
    val fullName: String,
    val email: String,
    val createdAt: String?,
    val updatedAt: String?
)