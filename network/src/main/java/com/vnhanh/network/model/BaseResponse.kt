package com.vnhanh.network.model

import com.google.gson.annotations.SerializedName

abstract class BaseResponse<T>(
    @SerializedName("code") val code: Int? = null,
    @SerializedName("data") val data: T? = null,
    @SerializedName("message") val message: String? = null,
    @SerializedName("errors") val errors: ApiError? = null
)

data class ApiError(
    @SerializedName("field_errors")  val fieldErrors: Map<String, List<String>>? = null,
)
