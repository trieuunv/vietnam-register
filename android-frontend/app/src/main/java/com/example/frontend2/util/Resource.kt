package com.example.frontend2.util

sealed class Resource<out T> {
    data class Success<T>(val data: T) : Resource<T>()
    data class Error(val exception: Throwable, val errorData: Any? = null) : Resource<Nothing>()
    data object Loading : Resource<Nothing>()
}