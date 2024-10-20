package com.vnhanh.authentication.api

import com.vnhanh.authentication.model.LoginRequest
import com.vnhanh.authentication.model.LoginResponse
import com.vnhanh.network.model.BaseResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface IAuthenticationApi {
    @POST("login")
    @FormUrlEncoded
    suspend fun login(
        @Body request: LoginRequest
    ) : Response<BaseResponse<LoginResponse>>

    @POST("register")
    @FormUrlEncoded
    suspend fun register() : Response<BaseResponse<LoginResponse>>
}
