package com.lyhorng.sampleapp.data.model.response

data class OrderCreateResponse(
    val success: Boolean,
    val message: String,
    val data: Order
)
