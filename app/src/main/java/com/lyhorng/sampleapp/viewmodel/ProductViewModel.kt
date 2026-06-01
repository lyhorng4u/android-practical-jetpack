package com.lyhorng.sampleapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lyhorng.sampleapp.data.model.response.Category
import com.lyhorng.sampleapp.data.model.response.Product
import com.lyhorng.sampleapp.data.remote.ApiResult
import com.lyhorng.sampleapp.data.repository.CategoryRepository
import com.lyhorng.sampleapp.data.repository.ProductRepository
import com.lyhorng.sampleapp.viewmodel.utils.Resource
import kotlinx.coroutines.launch

class ProductViewModel : ViewModel() {

    private val productRepository = ProductRepository()
    private val categoryRepository = CategoryRepository()

    private val _productsResult = MutableLiveData<Resource<List<Product>>>()
    val productsResult: LiveData<Resource<List<Product>>> = _productsResult

    private val _productDetailResult = MutableLiveData<Resource<Product>>()
    val productDetailResult: LiveData<Resource<Product>> = _productDetailResult

    private val _categoriesResult = MutableLiveData<Resource<List<Category>>>()
    val categoriesResult: LiveData<Resource<List<Category>>> = _categoriesResult

    fun getProducts(page: Int = 1, limit: Int = 10, search: String? = null, categoryId: Int? = null) {
        viewModelScope.launch {
            _productsResult.value = Resource.Loading()
            when (val result = productRepository.getProducts(page, limit, search, categoryId)) {
                is ApiResult.Success -> {
                    _productsResult.value = Resource.Success(result.data.data)
                }
                is ApiResult.Error -> {
                    _productsResult.value = Resource.Error(result.message)
                }
            }
        }
    }

    fun getProductById(productId: Int) {
        viewModelScope.launch {
            _productDetailResult.value = Resource.Loading()
            when (val result = productRepository.getProductById(productId)) {
                is ApiResult.Success -> {
                    _productDetailResult.value = Resource.Success(result.data.data)
                }
                is ApiResult.Error -> {
                    _productDetailResult.value = Resource.Error(result.message)
                }
            }
        }
    }

    fun getCategories() {
        viewModelScope.launch {
            _categoriesResult.value = Resource.Loading()
            when (val result = categoryRepository.getCategories()) {
                is ApiResult.Success -> {
                    _categoriesResult.value = Resource.Success(result.data.data)
                }
                is ApiResult.Error -> {
                    _categoriesResult.value = Resource.Error(result.message)
                }
            }
        }
    }
}