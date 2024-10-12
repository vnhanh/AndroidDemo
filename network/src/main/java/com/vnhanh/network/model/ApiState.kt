package com.vnhanh.network.model

sealed class ApiState<out Success> {
    data object Loading : ApiState<Nothing>()

    data class Success<Success>(val data: Success) : ApiState<Success>()

    data class Error(
        val code: Int = -1,
        val type: ApiErrorType = ApiErrorType.UNKNOWN,
        val message: String = "",
        val errors: Map<String, List<String>>? = null,
    ) : ApiState<Nothing>()
}

enum class ApiErrorType {
    BODY_NULL,
    PARSE,
    TIMEOUT,
    UNKNOWN_HOST,
    UNAUTHORIZED,
    UNKNOWN,
}
