package com.vnhanh.network.httpLog

import android.text.TextUtils
import com.vnhanh.common.data.log.printDebugStackTrace
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody
import okhttp3.internal.platform.Platform
import java.io.IOException
import java.util.concurrent.Executor
import java.util.concurrent.TimeUnit


internal interface IBufferListener {
    @Throws(IOException::class)
    fun getJsonResponse(var1: Request?): String?
}

class AppLoggingInterceptor private constructor(private val builder: Builder) : Interceptor {
    private var isDebug: Boolean

    init {
        isDebug = builder.isDebug
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        var request: Request = chain.request()
        val headerMap: java.util.HashMap<String?, String?> = builder.headers
        var rSubtype: String?
        var key: String?
        if (headerMap.size > 0) {
            val requestBuilder: Request.Builder = request.newBuilder()
            for (o: String? in headerMap.keys) {
                rSubtype = o
                key = headerMap[rSubtype]
                if (!rSubtype.isNullOrBlank()) {
                    requestBuilder.addHeader(rSubtype, key.orEmpty())
                }
            }
            request = requestBuilder.build()
        }
        val queryMap: java.util.HashMap<String?, String> = builder.httpUrl
        if (queryMap.size > 0) {
            val httpUrlBuilder: HttpUrl.Builder? = request.url.newBuilder(request.url.toString())
            for (o: String? in queryMap.keys) {
                key = o
                val value: String? = queryMap[key]
                key?.also { interKey ->
                    httpUrlBuilder?.addQueryParameter(interKey, value.orEmpty())
                }
            }
            request = request.newBuilder().url(httpUrlBuilder?.build() ?: HttpUrl.Builder().build()).build()
        }

        return if (isDebug && builder.getLevel() !== ILogger.Level.NONE) {
            val requestBody: RequestBody? = request.body
            rSubtype = null
            requestBody?.contentType()?.also { contentType ->
                rSubtype = contentType.subtype
            }

            val executor: Executor? = builder.executor

            if (isFormUrlEncoded(rSubtype)) {
                AppPrinter.printJsonRequestDecode(builder, request)
            } else {
                if (isNotFileRequest(rSubtype)) {
                    if (executor != null) {
                        executor.execute(
                            /* command = */ createPrintJsonRequestRunnable(
                                builder = builder, request = request
                            )
                        )
                    } else {
                        AppPrinter.printJsonRequest(builder = builder, request = request)
                    }
                } else if (executor != null) {
                    executor.execute(/* command = */ createFileRequestRunnable(
                        builder = builder,
                        request = request
                    )
                    )
                } else {
                    AppPrinter.printFileRequest(builder = builder, request = request)
                }
            }

            val st: Long = System.nanoTime()

            val response: Response = if (builder.isMockEnabled) {
                try {
                    TimeUnit.MILLISECONDS.sleep(builder.sleepMs)
                } catch (var24: InterruptedException) {
                    var24.printDebugStackTrace()
                }

                Response.Builder()
                    .body(
                        builder.listener?.getJsonResponse(request).orEmpty()
                            .toResponseBody("application/json".toMediaTypeOrNull())
                    )
                    .request(chain.request()).protocol(Protocol.HTTP_2)
                    .message("Mock")
                    .code(200)
                    .build()
            } else {
                chain.proceed(request)
            }

            val chainMs: Long = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - st)
            val segmentList: List<String> = request.url.encodedPathSegments
            val header: String = response.headers.toString()
            val code: Int = response.code
            val isSuccessful: Boolean = response.isSuccessful
            val message: String = response.message
            val responseBody: ResponseBody? = response.body
            val contentType: MediaType? = responseBody?.contentType()
            var subtype: String? = null

            if (contentType != null) {
                subtype = contentType.subtype
            }

            if (isNotFileRequest(subtype)) {
                val bodyString: String =
                    AppPrinter.getJsonString(responseBody?.string().orEmpty()).orEmpty()
                val url: String = response.request.url.toString()
                if (executor != null) {
                    executor.execute(
                        /* command = */ createPrintJsonResponseRunnable(
                            builder = builder,
                            chainMs = chainMs,
                            isSuccessful = isSuccessful,
                            code = code,
                            headers = header,
                            bodyString = bodyString,
                            segments = segmentList,
                            message = message,
                            responseUrl = url
                        )
                    )
                } else {
                    AppPrinter.printJsonResponse(
                        builder = builder,
                        chainMs = chainMs,
                        isSuccessful = isSuccessful,
                        code = code,
                        headers = header,
                        bodyString = bodyString,
                        segments = segmentList,
                        message = message,
                        responseUrl = url
                    )
                }

                val body: ResponseBody = bodyString.toResponseBody(contentType)
                response.newBuilder().body(body).build()
            } else {
                if (executor != null) {
                    executor.execute(
                        /* command = */ createFileResponseRunnable(
                            builder = builder,
                            chainMs = chainMs,
                            isSuccessful = isSuccessful,
                            code = code,
                            headers = header,
                            segments = segmentList,
                            message = message
                        )
                    )
                } else {
                    AppPrinter.printFileResponse(
                        builder = builder,
                        chainMs = chainMs,
                        isSuccessful = isSuccessful,
                        code = code,
                        headers = header,
                        segments = segmentList,
                        message = message
                    )
                }
                response
            }
        } else {
            chain.proceed(request)
        }
    }

    private fun isNotFileRequest(subtype: String?): Boolean {
        return subtype != null && (subtype.contains("json") || subtype.contains("xml") || subtype.contains(
            "plain"
        ) || subtype.contains("html"))
    }

    private fun isFormUrlEncoded(subtype: String?): Boolean {
        return subtype != null && subtype.contains("urlencoded") //x-www-form-urlencoded
    }

    internal class Builder {
        var headers: HashMap<String?, String?> = hashMapOf()
        var httpUrl: HashMap<String?, String> = hashMapOf()
        var isLogHackEnable = false
            private set

        var isDebug = false
            private set

        var type: Int = Platform.INFO
            private set

        private var requestTag: String? = null
        private var responseTag: String? = null
        private var level: ILogger.Level
        private var logger: ILogger = ILogger.DEFAULT
        var executor: Executor? = null
            private set
        var isMockEnabled = false
            private set

        var sleepMs: Long = 0
            private set

        var listener: IBufferListener? = null
            private set

        var isAllowCopy = true
            private set

        init {
            level = ILogger.Level.BASIC
            headers = HashMap()
            httpUrl = HashMap()
        }

        fun getLevel(): ILogger.Level {
            return level
        }

        fun setLevel(level: ILogger.Level): Builder {
            this.level = level
            return this
        }

        fun getTag(isRequest: Boolean): String {
            return if (isRequest) {
                if (TextUtils.isEmpty(requestTag)) TAG else requestTag!!
            } else {
                if (TextUtils.isEmpty(responseTag)) TAG else responseTag!!
            }
        }

        fun getLogger(): ILogger {
            return logger
        }

        fun addHeader(name: String?, value: String?): Builder {
            headers[name] = value
            return this
        }

        fun addQueryParam(name: String?, value: String): Builder {
            httpUrl[name] = value
            return this
        }

        fun tag(tag: String): Builder {
            TAG = tag
            return this
        }

        fun request(tag: String?): Builder {
            requestTag = tag
            return this
        }

        fun response(tag: String?): Builder {
            responseTag = tag
            return this
        }

        fun loggable(isDebug: Boolean): Builder {
            this.isDebug = isDebug
            return this
        }

        fun log(type: Int): Builder {
            this.type = type
            return this
        }

        fun logger(logger: ILogger): Builder {
            this.logger = logger
            return this
        }

        fun executor(executor: Executor?): Builder {
            this.executor = executor
            return this
        }

        fun enableMock(useMock: Boolean, sleep: Long, listener: IBufferListener?): Builder {
            isMockEnabled = useMock
            sleepMs = sleep
            this.listener = listener
            return this
        }

        fun enableAndroidStudioV3LogsHack(useHack: Boolean): Builder {
            isLogHackEnable = useHack
            return this
        }

        fun enableLogModeForCopy(isCopy: Boolean): Builder {
            isAllowCopy = isCopy
            return this
        }

        fun build(): AppLoggingInterceptor {
            return AppLoggingInterceptor(this)
        }

        companion object {
            private var TAG = "LoggingI"
        }
    }

    companion object {
        private fun createPrintJsonRequestRunnable(builder: Builder, request: Request): Runnable {
            return Runnable { AppPrinter.printJsonRequest(builder, request) }
        }

        private fun createFileRequestRunnable(builder: Builder, request: Request): Runnable {
            return Runnable { AppPrinter.printFileRequest(builder, request) }
        }

        private fun createPrintJsonResponseRunnable(
            builder: Builder,
            chainMs: Long,
            isSuccessful: Boolean,
            code: Int,
            headers: String,
            bodyString: String,
            segments: List<String>,
            message: String,
            responseUrl: String
        ): Runnable {
            return Runnable {
                AppPrinter.printJsonResponse(
                    builder,
                    chainMs,
                    isSuccessful,
                    code,
                    headers,
                    bodyString,
                    segments,
                    message,
                    responseUrl
                )
            }
        }

        private fun createFileResponseRunnable(
            builder: Builder,
            chainMs: Long,
            isSuccessful: Boolean,
            code: Int,
            headers: String,
            segments: List<String>,
            message: String
        ): Runnable {
            return Runnable {
                AppPrinter.printFileResponse(
                    builder,
                    chainMs,
                    isSuccessful,
                    code,
                    headers,
                    segments,
                    message
                )
            }
        }
    }
}
