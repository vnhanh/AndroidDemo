package com.vnhanh.authentication.data

import com.vnhanh.authentication.api.IAuthenticationApi
import com.vnhanh.authentication.model.LoginRequest
import com.vnhanh.authentication.model.LoginResponse
import com.vnhanh.network.model.ApiState
import com.vnhanh.network.util.IApiLauncher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

// If user credentials will be cached in local storage, it is recommended it be encrypted
// @see https://developer.android.com/training/articles/keystore
interface IAuthRepository {
    fun login(username: String, password: String, userDeviceKey: String): Flow<ApiState<LoginResponse>>
}

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */
class AuthenticationRepository(
    private val api: IAuthenticationApi,
    private val launcher: IApiLauncher,
) : IAuthRepository {
    override fun login(
        username: String,
        password: String,
        userDeviceKey: String,
    ): Flow<ApiState<LoginResponse>> = launcher(
        apiCall = {
            val request = LoginRequest(
                email = username,
                password = password,
                userDeviceKey = userDeviceKey,
            )

            api.login(request)
        }
    )

}
