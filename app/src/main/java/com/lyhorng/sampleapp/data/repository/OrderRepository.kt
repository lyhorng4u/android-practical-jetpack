package com.lyhorng.sampleapp.data.repository

import com.lyhorng.sampleapp.data.model.request.CreateOrderRequest
import com.lyhorng.sampleapp.data.remote.ApiResult
import com.lyhorng.sampleapp.data.remote.BaseService
import com.lyhorng.sampleapp.data.model.response.OrderCreateResponse
import com.lyhorng.sampleapp.data.model.response.OrderListResponse

class OrderRepository {

    private val apiService = BaseService.apiService

    suspend fun getOrders(
        page: Int = 1,
        limit: Int = 10,
        userId: Int? = null
    ): ApiResult<OrderListResponse> {
        return BaseService.safeApiCall {
            apiService.getOrders(page, limit, userId)
        }
    }

    suspend fun createOrder(
        userId: Int,
        productId: Int,
        quantity: Int
    ): ApiResult<OrderCreateResponse> {
        val request = CreateOrderRequest(userId, productId, quantity)
        return BaseService.safeApiCall {
            apiService.createOrder(request)
        }
    }
}