package com.lyhorng.sampleapp.data.repository

import com.lyhorng.sampleapp.data.remote.ApiResult
import com.lyhorng.sampleapp.data.remote.BaseService
import com.lyhorng.sampleapp.data.model.response.DashboardResponse

class DashboardRepository {

    private val apiService = BaseService.apiService

    suspend fun getDashboardStats(): ApiResult<DashboardResponse> {
        return BaseService.safeApiCall {
            apiService.getDashboardStats()
        }
    }
}