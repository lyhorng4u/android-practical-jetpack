package com.lyhorng.sampleapp.data.model.request

data class CreateOrderRequest(
    val userId: Int,
    val productId: Int,
    val quantity: Int
)