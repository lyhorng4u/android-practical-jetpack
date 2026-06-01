package com.lyhorng.sampleapp.data.model.response

//data class AuthResponse (
//    val success: Boolean,
//    val  message: String,
//    val token: String,
//    val user: User?
//)

data class AuthResponse(
    val success: Boolean,
    val message: String,
    val data: AuthData   // ✅ FIX HERE
)