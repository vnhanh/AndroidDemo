package com.vnhanh.core.network

import com.google.gson.annotations.SerializedName

data class BaseResponse<T>(
    @SerializedName("code") val code: Int?,
    @SerializedName("message") val message: String?,
    @SerializedName("data") val data: T?,
    @SerializedName("page_meta") val pageMetadata: T?,
    @SerializedName("page_link") val pageLink: T?,
)

/**
 * @param total the total number of items available on the server
 * @param count the number of items returned by this call
 * @param perPage the number of items per page
 * @param currentPage the current page number
 * @param totalPages the total number of pages available
 */
data class PagingMeta(
    @SerializedName("total") val total: Int?,
    @SerializedName("count") val count: Int?,
    @SerializedName("per_page") val perPage: Int?,
    @SerializedName("current_page") val currentPage: Int?,
    @SerializedName("total_pages") val totalPages: Int?,
)

data class PageLink(
    @SerializedName("first_page") val firstPage: String?,
    @SerializedName("last_page") val lastPage: String?,
    @SerializedName("prev_page") val prevPage: String?,
    @SerializedName("next_page") val nextPage: String?,
)
