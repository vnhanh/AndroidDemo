package com.vnhanh.network.model.authentication

import com.google.gson.annotations.SerializedName

/**
 * @param membership refer to [MEMBERSHIP_FREE], [MEMBERSHIP_TRIAL], [MEMBERSHIP_PREMIUM]
 */
data class LoginResponse(
    @SerializedName("user_id") val userId: Int? = null,
    @SerializedName("token") val token: String? = null,
    @SerializedName("refresh_token") val refreshToken: String? = null,
    @SerializedName("token_expired") val tokenExpired: String? = null,
    @SerializedName("refresh_token_expired") val refreshTokenExpired: String? = null,
    @SerializedName("membership") val membership: Int? = null,
    @SerializedName("role") val role: Int? = null,
    @SerializedName("avatar") val avatar: String? = null,
    @SerializedName("full_name") val fullName: String? = null,
    @SerializedName("email") val email: String? = null,
    @SerializedName("address") val address: String? = null,
    @SerializedName("gender") val gender: Boolean? = null,
    @SerializedName("created_at") val createdAt: String? = null,
)

const val MEMBERSHIP_FREE = 0
const val MEMBERSHIP_TRIAL = 1
const val MEMBERSHIP_PREMIUM = 2
