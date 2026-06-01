package com.lyhorng.sampleapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lyhorng.sampleapp.data.model.response.Order
import com.lyhorng.sampleapp.data.remote.ApiResult
import com.lyhorng.sampleapp.data.repository.OrderRepository
import com.lyhorng.sampleapp.viewmodel.utils.Resource
import kotlinx.coroutines.launch

class OrderViewModel : ViewModel() {

    private val repository = OrderRepository()

    private val _ordersResult = MutableLiveData<Resource<List<Order>>>()
    val ordersResult: LiveData<Resource<List<Order>>> = _ordersResult

    private val _createOrderResult = MutableLiveData<Resource<Order>>()
    val createOrderResult: LiveData<Resource<Order>> = _createOrderResult

    fun getOrders(page: Int = 1, limit: Int = 10, userId: Int? = null) {
        viewModelScope.launch {
            _ordersResult.value = Resource.Loading()
            when (val result = repository.getOrders(page, limit, userId)) {
                is ApiResult.Success -> {
                    _ordersResult.value = Resource.Success(result.data.data)
                }
                is ApiResult.Error -> {
                    _ordersResult.value = Resource.Error(result.message)
                }
            }
        }
    }

    fun createOrder(userId: Int, productId: Int, quantity: Int) {
        viewModelScope.launch {
            _createOrderResult.value = Resource.Loading()
            when (val result = repository.createOrder(userId, productId, quantity)) {
                is ApiResult.Success -> {
                    _createOrderResult.value = Resource.Success(result.data.data)
                }
                is ApiResult.Error -> {
                    _createOrderResult.value = Resource.Error(result.message)
                }
            }
        }
    }
}