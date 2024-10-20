package com.vnhanh.authentication.model

import com.google.gson.annotations.SerializedName
import com.vnhanh.network.model.BaseRequest


data class LoginRequest(
    @SerializedName("email") val email: String? = null,
    @SerializedName("password") val password: String,
    @SerializedName("user_device_key") val userDeviceKey: String,
) : BaseRequest()

data class LoginResponse(
    @SerializedName("user_id") val userId: String? = null,
    @SerializedName("token") val token: String? = null,
    @SerializedName("token_expired") val tokenExpired: String? = null,
    @SerializedName("refresh_token") val refreshToken: String? = null,
    @SerializedName("refresh_token_expired") val refreshTokenExpired: String? = null,
    @SerializedName("avatar_url") val avatarUrl: String? = null,
    @SerializedName("display_name") val displayName: String? = null,
    @SerializedName("user_name") val userName: String? = null,
    @SerializedName("email") val email: String? = null,
    @SerializedName("address") val address: String? = null,
    @SerializedName("first_name") val firstName: String? = null,
    @SerializedName("last_name") val lastName: String? = null,
    @SerializedName("phone_number") val phoneNumber: String? = null,
    @SerializedName("member") val member: Int? = null,
)

const val MEMBER_FREE = 0
const val MEMBER_ELITE = 1
const val MEMBER_PREMIUM = 2
