package com.lyhorng.sampleapp.data.remote

import com.lyhorng.sampleapp.data.model.request.*
import com.lyhorng.sampleapp.data.model.response.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    // ==================== AUTH ENDPOINTS ====================

    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<AuthResponse>

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<AuthResponse>

    @GET("auth/me")
    suspend fun getCurrentUser(): Response<UserResponse>

    @GET("auth/profile")
    suspend fun getProfile(): Response<UserResponse>

    @PUT("auth/profile")
    suspend fun updateProfile(@Body request: UpdateProfileRequest): Response<UserResponse>

    @POST("auth/change-password")
    suspend fun changePassword(@Body request: ChangePasswordRequest): Response<AuthResponse>

    // ==================== PRODUCT ENDPOINTS ====================

    @GET("products")
    suspend fun getProducts(
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 10,
        @Query("search") search: String? = null,
        @Query("categoryId") categoryId: Int? = null
    ): Response<ProductListResponse>

    @GET("products/{id}")
    suspend fun getProductById(@Path("id") productId: Int): Response<ProductDetailResponse>

    // ==================== CATEGORY ENDPOINTS ====================

    @GET("categories")
    suspend fun getCategories(): Response<CategoryListResponse>

    // ==================== ORDER ENDPOINTS ====================

    @GET("orders")
    suspend fun getOrders(
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 10,
        @Query("userId") userId: Int? = null
    ): Response<OrderListResponse>

    @POST("orders")
    suspend fun createOrder(@Body request: CreateOrderRequest): Response<OrderCreateResponse>

    // ==================== DASHBOARD ENDPOINTS ====================

    @GET("dashboard")
    suspend fun getDashboardStats(): Response<DashboardResponse>
}