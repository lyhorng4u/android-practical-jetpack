package com.lyhorng.sampleapp.data.model.response

data class OrderListResponse(
    val success: Boolean,
    val data: List<Order>,
    val pagination: Pagination?
)