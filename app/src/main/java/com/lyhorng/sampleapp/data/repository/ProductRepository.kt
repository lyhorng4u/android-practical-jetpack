package com.lyhorng.sampleapp.data.repository

import com.lyhorng.sampleapp.data.remote.ApiResult
import com.lyhorng.sampleapp.data.remote.BaseService
import com.lyhorng.sampleapp.data.model.response.ProductDetailResponse
import com.lyhorng.sampleapp.data.model.response.ProductListResponse

class ProductRepository {

    private val apiService = BaseService.apiService

    suspend fun getProducts(
        page: Int = 1,
        limit: Int = 10,
        search: String? = null,
        categoryId: Int? = null
    ): ApiResult<ProductListResponse> {
        return BaseService.safeApiCall {
            apiService.getProducts(page, limit, search, categoryId)
        }
    }

    suspend fun getProductById(productId: Int): ApiResult<ProductDetailResponse> {
        return BaseService.safeApiCall {
            apiService.getProductById(productId)
        }
    }
}