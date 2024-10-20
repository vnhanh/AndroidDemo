package com.vnhanh.authentication.domain

import com.vnhanh.authentication.data.IAuthDataSource
import com.vnhanh.authentication.data.IAuthRepository
import com.vnhanh.authentication.model.LoginResponse
import com.vnhanh.authentication.model.UserData
import com.vnhanh.authentication.model.toUserData
import com.vnhanh.network.model.ApiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface ILoginUseCase {
    fun login(email: String, password: String) : Flow<ApiState<UserData>>
}

class LoginUseCase(
    private val authRepository: IAuthRepository,
    private val authDataSource: IAuthDataSource,
) : ILoginUseCase {
    override fun login(email: String, password: String): Flow<ApiState<UserData>> =
        authRepository.login(
            username = email,
            password = password,
            userDeviceKey = authDataSource.getUserDeviceKey()
        ).map { apiState: ApiState<LoginResponse> ->
            val userData = (apiState as? ApiState.Success<LoginResponse>)?.data?.toUserData()
            when {
                apiState is ApiState.Success<LoginResponse> && userData != null -> {
                    ApiState.Success(userData)
                }

                apiState is ApiState.Success<LoginResponse> -> {
                    ApiState.Error(message = "Login failed")
                }

                apiState is ApiState.Error -> apiState

                apiState is ApiState.Loading -> apiState

                else -> ApiState.Error(message = "Login failed")
            }
        }
}
