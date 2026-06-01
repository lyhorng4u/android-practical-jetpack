package com.lyhorng.sampleapp.data.model.response

data class Order(
    val id: Int,
    val userId: Int,
    val productId: Int,
    val quantity: Int,
    val totalPrice: Double,
    val status: String,
    val createdAt: String,
    val product: Product?,
    val user: User?
)
