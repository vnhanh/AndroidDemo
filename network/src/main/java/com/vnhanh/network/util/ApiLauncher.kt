package com.vnhanh.network.util

import com.vnhanh.network.common.ApiConstant
import com.vnhanh.network.model.ApiErrorType
import com.vnhanh.network.model.ApiState
import com.vnhanh.network.model.BaseResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import retrofit2.Response
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

interface IApiLogger {
    fun log(throwable: Throwable, tag: String?)
}

interface IApiLauncher {
    operator fun<T> invoke(
        apiCall: suspend () -> Response<BaseResponse<T>>,
        tag: String = "",
    ) : Flow<ApiState<T>>
}

class ApiLauncherImpl @Inject constructor(
    private val logger: IApiLogger,
) : IApiLauncher {
    override operator fun<T> invoke(
        apiCall: suspend () -> Response<BaseResponse<T>>,
        tag: String,
    ): Flow<ApiState<T>> = flow {
        val response: Response<BaseResponse<T>> = apiCall()

        when {
            response.isSuccessful -> {
                val parsedState: ApiState<T> = parseDataBody(response)
                emit(parsedState)
            }

            else -> {
                val error = ApiState.Error(
                    code = response.code(),
                    message = response.message(),
                )
                emit(error)
            }
        }
    }
    .onStart {
        emit(ApiState.Loading)
    }
    .catch { e ->
        if (e !is CancellationException) {
            logger.log(e, tag)
            val error: ApiState<T> = handleException(e)
            emit(error)
        }
    }

    private fun<T> handleException(e: Throwable) : ApiState<T> {
        logger.log(e, "API ERROR")
        return when (e) {
            is java.net.UnknownHostException -> {
                ApiState.Error(type = ApiErrorType.UNKNOWN_HOST)
            }

            is java.net.SocketTimeoutException -> {
                ApiState.Error(type = ApiErrorType.TIMEOUT)
            }

            is com.google.gson.JsonSyntaxException -> {
                ApiState.Error(type = ApiErrorType.PARSE)
            }

            else -> {
                ApiState.Error(type = ApiErrorType.UNKNOWN, message = e.message.orEmpty())
            }
        }
    }

    private fun <T> parseDataBody(
        response: Response<BaseResponse<T>>
    ): ApiState<T> {
        val apiResponse: BaseResponse<T> = response.body() ?: return ApiState.Error(type = ApiErrorType.BODY_NULL)
        val apiCode = apiResponse.code ?: return ApiState.Error(type = ApiErrorType.UNKNOWN)
        val errors: Map<String, List<String>>? = apiResponse.errors
        val successData = apiResponse.data

        return when {
            apiCode == ApiConstant.CODE_SUCCESS && successData != null -> {
                ApiState.Success(data = successData)
            }

            apiCode == ApiConstant.CODE_SUCCESS && successData == null -> {
                ApiState.Error(type = ApiErrorType.PARSE)
            }

            apiCode == ApiConstant.CODE_UNAUTHORIZED -> {
                ApiState.Error(type = ApiErrorType.UNAUTHORIZED)
            }

            else -> {
                ApiState.Error(
                    message = apiResponse.message.orEmpty(),
                    errors = errors,
                )
            }
        }
    }
}
