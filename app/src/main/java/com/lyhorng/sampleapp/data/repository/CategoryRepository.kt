package com.lyhorng.sampleapp.data.repository

import com.lyhorng.sampleapp.data.remote.ApiResult
import com.lyhorng.sampleapp.data.remote.BaseService
import com.lyhorng.sampleapp.data.model.response.CategoryListResponse

class CategoryRepository {

    private val apiService = BaseService.apiService

    suspend fun getCategories(): ApiResult<CategoryListResponse> {
        return BaseService.safeApiCall {
            apiService.getCategories()
        }
    }
}