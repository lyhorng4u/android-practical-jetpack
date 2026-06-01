package com.lyhorng.sampleapp.data.model.response

data class Product(
    val id: Int,
    val name: String,
    val description: String,
    val price: Double,
    val categoryId: Int,
    val imageUrl: String,
    val stock: Int?,
    val category: Category?
)
