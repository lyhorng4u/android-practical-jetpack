package com.lyhorng.sampleapp.data.remote

import com.lyhorng.sampleapp.viewmodel.utils.Constants
import com.lyhorng.sampleapp.viewmodel.utils.PreferenceManager
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object BaseService {

    private var preferenceManager: PreferenceManager? = null

    fun initialize(prefManager: PreferenceManager) {
        preferenceManager = prefManager
    }

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val authInterceptor = Interceptor { chain ->
        val request = chain.request()
        val requestBuilder = request.newBuilder()

        // Add Authorization header if token exists
        preferenceManager?.getToken()?.let { token ->
            requestBuilder.addHeader("Authorization", "Bearer $token")
        }

        chain.proceed(requestBuilder.build())
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .addInterceptor(authInterceptor)
        .connectTimeout(Constants.TIMEOUT_CONNECT, TimeUnit.SECONDS)
        .readTimeout(Constants.TIMEOUT_READ, TimeUnit.SECONDS)
        .writeTimeout(Constants.TIMEOUT_WRITE, TimeUnit.SECONDS)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(Constants.BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiService: ApiService = retrofit.create(ApiService::class.java)

    /**
     * Safe API call wrapper with exception handling
     */
    suspend fun <T> safeApiCall(apiCall: suspend () -> Response<T>): ApiResult<T> {
        return try {
            val response = apiCall()
            if (response.isSuccessful) {
                ApiResult.Success(response.body()!!)
            } else {
                val errorMessage = response.errorBody()?.string() ?: "Unknown error occurred"
                ApiResult.Error(errorMessage, response.code())
            }
        } catch (e: Exception) {
            ApiResult.Error(e.message ?: "Network error occurred", null)
        }
    }
}

/**
 * Sealed class for API result handling
 */
sealed class ApiResult<out T> {
    data class Success<T>(val data: T) : ApiResult<T>()
    data class Error(val message: String, val code: Int?) : ApiResult<Nothing>()
}