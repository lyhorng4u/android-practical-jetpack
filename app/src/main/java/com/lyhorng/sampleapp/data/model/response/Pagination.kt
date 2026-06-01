package com.lyhorng.sampleapp.data.model.response

data class Pagination (
    val page: Int,
    val limit: Int,
    val total: Int,
    val totalPages: Int
)