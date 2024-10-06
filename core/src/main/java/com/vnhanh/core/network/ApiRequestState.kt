package com.vnhanh.core.network

sealed class ApiRequestState {
    data object Loading : ApiRequestState()
    data class Success<T>(val data: T) : ApiRequestState()

    /**
     * @param code the api response code that indicates a certain error in some cases
     * @param message the error message that might be displayed to the user
     */
    data class Error(
        val code: Int,
        val message: String,
    ) : ApiRequestState()

    val isLoading : Boolean
        get() = this is Loading

    val isSuccess : Boolean
        get() = this is Success<*>

    val isError : Boolean
        get() = this is Error
}
