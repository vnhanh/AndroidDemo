package com.vnhanh.network.util

import com.vnhanh.common.data.log.printDebugStackTrace
import com.vnhanh.network.common.ApiConstant
import com.vnhanh.network.model.ApiError
import com.vnhanh.network.model.BaseResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import kotlin.coroutines.cancellation.CancellationException

interface IApiLauncher {
    fun<T> launch(
        apiCall: suspend () -> Response<BaseResponse<T>>,
        tag: String? = null,
    ) : Flow<ApiResult>
}

interface IApiLogger {
    fun log(throwable: Throwable, tag: String?)
}

enum class ResponseCode {
    BODY_NULL,
    UNMATCHED,
}

sealed class ApiResult {
    data class Success<T>(val data: T) : ApiResult()

    data class Failure(
        val responseCode: Int = ApiConstant.CODE_ERROR_UNKNOWN,
        val errors: ApiError? = null,
        val message: String? = null,
    ) : ApiResult()

    data class Unauthorized(val message: String?) : ApiResult()

    data class ParseException(
        val code: ResponseCode,
        val message: String? = null,
    ) : ApiResult()

    data object UnknownHost : ApiResult()
    data object Timeout : ApiResult()
    data class UnknownError(val message: String? = null) : ApiResult()
}

class ApiLauncherImpl constructor(
    private val logger: IApiLogger,
) : IApiLauncher {
    override fun <T> launch(
        apiCall: suspend () -> Response<BaseResponse<T>>,
        tag: String?,
    ): Flow<ApiResult> = flow{
        val response: Response<BaseResponse<T>> = apiCall()

        when {
            response.isSuccessful -> {
                val parsedResult: ApiResult = parseDataBody(response)
                emit(parsedResult)
            }

            else -> {
                val error = ApiResult.Failure(
                    responseCode = response.code(),
                    message = response.message(),
                )
                emit(error)
            }
        }
    }.catch { e ->
        if (e is CancellationException) return@catch
        e.printDebugStackTrace()
        logger.log(e, tag)
        val error: ApiResult = handleException(e)
        emit(error)
    }

    private fun handleException(e: Throwable) : ApiResult {
        return when (e) {
            is java.net.UnknownHostException -> {
                ApiResult.UnknownHost
            }

            is java.net.SocketTimeoutException -> {
                ApiResult.Timeout
            }

            is com.google.gson.JsonSyntaxException -> {
                ApiResult.ParseException(code = ResponseCode.UNMATCHED, message = e.message)
            }

            else -> {
                ApiResult.UnknownError(message = e.message)
            }
        }
    }

    private fun <T> parseDataBody(
        response: Response<BaseResponse<T>>
    ): ApiResult {
        val apiResponse: BaseResponse<T> = response.body() ?: return ApiResult.ParseException(code = ResponseCode.BODY_NULL)
        val apiCode = apiResponse.code ?: return ApiResult.ParseException(code = ResponseCode.BODY_NULL)
        val apiMessage: String? = apiResponse.message
        val apiErrors: ApiError? = apiResponse.errors

        return when (apiCode) {
            ApiConstant.CODE_SUCCESS -> {
                ApiResult.Success(data = apiResponse.data)
            }

            ApiConstant.CODE_UNAUTHORIZED -> {
                ApiResult.Unauthorized(message = apiMessage)
            }

            else -> {
                ApiResult.Failure(
                    // used for some cases, i.e exceed limit
                    errors = apiErrors,
                    message = apiMessage,
                )
            }
        }
    }
}
