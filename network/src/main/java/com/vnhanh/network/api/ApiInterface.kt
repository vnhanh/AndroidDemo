package com.vnhanh.network.api

import com.vnhanh.network.common.UrlConstant
import com.vnhanh.network.model.BaseResponse
import com.vnhanh.network.model.authentication.LoginRequest
import com.vnhanh.network.model.authentication.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiInterface {
    @POST(UrlConstant.LOGIN)
    suspend fun login(
        @Body request: LoginRequest
    ) : Response<BaseResponse<LoginResponse>>
}
