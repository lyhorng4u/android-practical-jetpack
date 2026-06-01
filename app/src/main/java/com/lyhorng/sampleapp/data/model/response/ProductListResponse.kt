package com.lyhorng.sampleapp.data.model.response

data class ProductListResponse(
    val success: Boolean,
    val data: List<Product>,
    val pagination: Pagination?
)