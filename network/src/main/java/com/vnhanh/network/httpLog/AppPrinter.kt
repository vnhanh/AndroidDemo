package com.vnhanh.network.httpLog

import android.text.TextUtils
import okhttp3.Request
import okhttp3.RequestBody
import okio.Buffer
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.io.UnsupportedEncodingException
import java.math.BigDecimal
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

internal object AppPrinter {
    private val LINE_SEPARATOR: String = System.getProperty("line.separator").orEmpty()
    private val DOUBLE_SEPARATOR: String = "$LINE_SEPARATOR$LINE_SEPARATOR"
    private val OMITTED_RESPONSE: Array<String> = arrayOf(LINE_SEPARATOR, "Omitted response body")
    private val OMITTED_REQUEST: Array<String> = arrayOf(LINE_SEPARATOR, "Omitted request body")
    private val OOM_OMITTED: String =
        LINE_SEPARATOR + "${LINE_SEPARATOR}Output omitted because of Object size."

    internal const val MAXIMUM_LENGTH_LOG_A_TAG = 2000

    private fun isEmpty(line: String): Boolean {
        return TextUtils.isEmpty(line) || "\n" == line || "\t" == line || TextUtils.isEmpty(line.trim { it <= ' ' })
    }

    private fun getRequestBodyStr(bodyToString: String?) =
        String.format("%sBody:%s%s", LINE_SEPARATOR, LINE_SEPARATOR, bodyToString.orEmpty())

    fun printJsonRequest(builder: AppLoggingInterceptor.Builder, request: Request) {
        val requestBody: String = getRequestBodyStr(bodyToString(request))
        val tag: String = builder.getTag(isRequest = true)
        builder.getLogger().log(
            typeLog = builder.type,
            tag = tag,
            message = "┌────── Request ────────────────────────────────────────────────────────────────────────",
            useLogHack = builder.isLogHackEnable
        )
        logLines(
            type = builder.type,
            tag = tag,
            lines = arrayOf("URL: " + request.url),
            logger = builder.getLogger(),
            withLineSize = false,
            useLogHack = builder.isLogHackEnable
        )
        logLines(
            type = builder.type,
            tag = tag,
            lines = getRequest(request, builder.getLevel()),
            logger = builder.getLogger(),
            withLineSize = true,
            useLogHack = builder.isLogHackEnable
        )
        if (builder.getLevel() === ILogger.Level.BASIC || builder.getLevel() === ILogger.Level.BODY) {
            if (builder.isAllowCopy) {
                logLinesForCopy(
                    type = builder.type,
                    tag = tag,
                    line = requestBody,
                    logger = builder.getLogger(),
                    withLineSize = true,
                    useLogHack = builder.isLogHackEnable
                )
            } else {
                logLines(
                    type = builder.type,
                    tag = tag,
                    lines = requestBody.split(LINE_SEPARATOR.toRegex())
                        .dropLastWhile { it.isEmpty() }
                        .toTypedArray(),
                    logger = builder.getLogger(),
                    withLineSize = true,
                    useLogHack = builder.isLogHackEnable)
            }
        }
        builder.getLogger().log(
            typeLog = builder.type,
            tag = tag,
            message = "└───────────────────────────────────────────────────────────────────────────────────────",
            useLogHack = builder.isLogHackEnable
        )
    }

    fun printJsonRequestDecode(
        builder: AppLoggingInterceptor.Builder,
        request: Request,
        logPrinter: ILogPrinter,
    ) {
        val requestBody: String = getRequestBodyStr(bodyToString(request))
        val tag: String = builder.getTag(isRequest = true)
        builder.getLogger().log(
            builder.type,
            tag,
            "┌────── Request ────────────────────────────────────────────────────────────────────────",
            builder.isLogHackEnable
        )
        logLines(
            type = builder.type,
            tag = tag,
            lines = arrayOf("URL: " + request.url),
            logger = builder.getLogger(),
            withLineSize = false,
            useLogHack = builder.isLogHackEnable
        )
        logLines(
            type = builder.type,
            tag = tag,
            lines = getRequest(request, builder.getLevel()),
            logger = builder.getLogger(),
            withLineSize = true,
            useLogHack = builder.isLogHackEnable
        )
        if (builder.getLevel() === ILogger.Level.BASIC || builder.getLevel() === ILogger.Level.BODY) {
            if (builder.isAllowCopy) {
                logLinesDecodeForCopy(
                    type = builder.type,
                    tag = tag,
                    line = requestBody,
                    logger = builder.getLogger(),
                    withLineSize = true,
                    useLogHack = builder.isLogHackEnable,
                )
            } else {
                logLinesDecode(
                    type = builder.type,
                    tag = tag,
                    lines = requestBody.split(LINE_SEPARATOR.toRegex()).dropLastWhile { it.isEmpty() }
                        .toTypedArray(),
                    logger = builder.getLogger(),
                    withLineSize = true,
                    useLogHack = builder.isLogHackEnable,
                    logPrinter = logPrinter,
                )
            }
        }
        builder.getLogger().log(
            typeLog = builder.type,
            tag = tag,
            message = "└───────────────────────────────────────────────────────────────────────────────────────",
            useLogHack = builder.isLogHackEnable,
        )
    }

    fun printJsonResponse(
        builder: AppLoggingInterceptor.Builder,
        chainMs: Long,
        isSuccessful: Boolean,
        code: Int,
        headers: String,
        bodyString: String,
        segments: List<String>,
        message: String,
        responseUrl: String
    ) {
        val responseBody: String = getRequestBodyStr(getJsonString(bodyString))
        val tag: String = builder.getTag(false)
        val urlLine: Array<String> = arrayOf("URL: $responseUrl", "\n")
        val response: Array<String> = getResponse(
            header = headers,
            tookMs = chainMs,
            code = code,
            isSuccessful = isSuccessful,
            segments = segments,
            message = message
        )
        builder.getLogger().log(
            typeLog = builder.type,
            tag = tag,
            message = "┌────── Response ───────────────────────────────────────────────────────────────────────",
            useLogHack = builder.isLogHackEnable
        )
        logLines(
            type = builder.type,
            tag = tag,
            lines = urlLine,
            logger = builder.getLogger(),
            withLineSize = true,
            useLogHack = builder.isLogHackEnable
        )
        logLines(
            type = builder.type,
            tag = tag,
            lines = response,
            logger = builder.getLogger(),
            withLineSize = true,
            useLogHack = builder.isLogHackEnable
        )
        if (builder.getLevel() === ILogger.Level.BASIC || builder.getLevel() === ILogger.Level.BODY) {
            if (code >= 500) {
                builder.getLogger().log(
                    typeLog = builder.type,
                    tag = tag,
                    message = "│ Server Error - Error Code: $code",
                    useLogHack = builder.isLogHackEnable
                )
            } else {
                if (builder.isAllowCopy) {
                    logLinesForCopy(
                        type = builder.type,
                        tag = tag,
                        line = responseBody,
                        logger = builder.getLogger(),
                        withLineSize = true,
                        useLogHack = builder.isLogHackEnable
                    )
                } else {
                    logLines(
                        type = builder.type,
                        tag = tag,
                        lines = responseBody.split(LINE_SEPARATOR.toRegex())
                            .dropLastWhile { it.isEmpty() }
                            .toTypedArray(),
                        logger = builder.getLogger(),
                        withLineSize = true,
                        useLogHack = builder.isLogHackEnable)
                }
            }
        }
        builder.getLogger().log(
            typeLog = builder.type,
            tag = tag,
            message = "└───────────────────────────────────────────────────────────────────────────────────────",
            useLogHack = builder.isLogHackEnable
        )
    }

    fun printFileRequest(builder: AppLoggingInterceptor.Builder, request: Request) {
        val tag: String = builder.getTag(true)
        builder.getLogger().log(
            typeLog = builder.type,
            tag = tag,
            message = "┌────── Request ────────────────────────────────────────────────────────────────────────",
            useLogHack = builder.isLogHackEnable
        )
        logLines(
            builder.type,
            tag,
            arrayOf<String>("URL: " + request.url),
            builder.getLogger(),
            false,
            builder.isLogHackEnable
        )
        logLines(
            builder.type,
            tag,
            getRequest(request, builder.getLevel()),
            builder.getLogger(),
            true,
            builder.isLogHackEnable
        )
        if (builder.getLevel() === ILogger.Level.BASIC || builder.getLevel() === ILogger.Level.BODY) {
            logLines(
                builder.type,
                tag,
                OMITTED_REQUEST,
                builder.getLogger(),
                true,
                builder.isLogHackEnable
            )
        }
        builder.getLogger().log(
            builder.type,
            tag,
            "└───────────────────────────────────────────────────────────────────────────────────────",
            builder.isLogHackEnable
        )
    }

    fun printFileResponse(
        builder: AppLoggingInterceptor.Builder,
        chainMs: Long,
        isSuccessful: Boolean,
        code: Int,
        headers: String,
        segments: List<String>,
        message: String
    ) {
        val tag: String = builder.getTag(false)
        builder.getLogger().log(
            typeLog = builder.type,
            tag = tag,
            message = "┌────── Response ───────────────────────────────────────────────────────────────────────",
            useLogHack = builder.isLogHackEnable
        )
        logLines(
            type = builder.type,
            tag = tag,
            lines = getResponse(
                header = headers,
                tookMs = chainMs,
                code = code,
                isSuccessful = isSuccessful,
                segments = segments,
                message = message
            ),
            logger = builder.getLogger(),
            withLineSize = true,
            useLogHack = builder.isLogHackEnable
        )
        logLines(
            type = builder.type,
            tag = tag,
            lines = OMITTED_RESPONSE,
            logger = builder.getLogger(),
            withLineSize = true,
            useLogHack = builder.isLogHackEnable
        )
        builder.getLogger().log(
            typeLog = builder.type,
            tag = tag,
            message = "└───────────────────────────────────────────────────────────────────────────────────────",
            useLogHack = builder.isLogHackEnable
        )
    }

    private fun getRequest(request: Request, level: ILogger.Level): Array<String> {
        val header: String = request.headers.toString()
        val loggableHeader: Boolean = level === ILogger.Level.HEADERS || level === ILogger.Level.BASIC
        val log: String =
            "Method: @" + request.method + DOUBLE_SEPARATOR + if (isEmpty(header)) "" else if (loggableHeader) "Headers:$LINE_SEPARATOR" + dotHeaders(
                header
            ) else ""
        return log.split(LINE_SEPARATOR.toRegex()).dropLastWhile { it.isEmpty() }
            .toTypedArray()
    }

    private fun getResponse(
        header: String,
        tookMs: Long,
        code: Int,
        isSuccessful: Boolean,
        segments: List<String>,
        message: String
    ): Array<String> {
        val loggableHeader = false
        val segmentString: String = slashSegments(segments)
        val log: String =
            (if (!TextUtils.isEmpty(segmentString)) "$segmentString - " else "") + "is success : " + isSuccessful + " - " + "Received in: " + tookMs + "ms" + DOUBLE_SEPARATOR + "Status Code: " + code + " / " + message + DOUBLE_SEPARATOR + if (isEmpty(
                    header
                )
            ) "" else if (loggableHeader) "Headers:" + LINE_SEPARATOR + dotHeaders(header) else ""
        return log.split(LINE_SEPARATOR.toRegex()).dropLastWhile { it.isEmpty() }
            .toTypedArray()
    }

    private fun slashSegments(segments: List<String>): String {
        val segmentString = StringBuilder()
        for (segment: String in segments) segmentString.append("/").append(segment)
        return segmentString.toString()
    }

    private fun dotHeaders(header: String): String {
        val headers: Array<String> =
            header.split(LINE_SEPARATOR.toRegex()).dropLastWhile { it.isEmpty() }
                .toTypedArray()
        val builder = StringBuilder()
        var tag = "─ "
        if (headers.size > 1) {
            for (i: Int in headers.indices) {
                tag = when (i) {
                    0 -> {
                        "┌ "
                    }

                    headers.size - 1 -> {
                        "└ "
                    }

                    else -> {
                        "├ "
                    }
                }
                builder.append(tag).append(headers[i]).append("\n")
            }
        } else {
            for (item: String in headers) {
                builder.append(tag).append(item).append("\n")
            }
        }

        return builder.toString()
    }

    private const val prefixNewLogLine = ""

    private fun logLines(
        type: Int,
        tag: String,
        lines: Array<String>,
        logger: ILogger,
        withLineSize: Boolean,
        useLogHack: Boolean
    ) {
        for (line: String in lines) {
            val lineLength: Int = line.length
            val maxLongSize: Int = if (withLineSize) 110 else lineLength
            for (i: Int in 0..lineLength / maxLongSize) {
                val start = i * maxLongSize
                var end = (i + 1) * maxLongSize
                end = if (end > line.length) line.length else end

                val prefix = if (i == 0) "| " else prefixNewLogLine
                logger.log(type, tag, prefix + line.substring(start, end), useLogHack)
            }
        }
    }

    private fun logLinesForCopy(
        type: Int,
        tag: String,
        line: String,
        logger: ILogger,
        withLineSize: Boolean,
        useLogHack: Boolean
    ) {
        val lineLength = line.length
        val maxLongNumber: Int = if (withLineSize) MAXIMUM_LENGTH_LOG_A_TAG else lineLength
        for (i in 0..lineLength / maxLongNumber) {
            val start = i * maxLongNumber
            var end = (i + 1) * maxLongNumber
            end = if (end > line.length) line.length else end

            val prefix = if (i == 0) "| " else prefixNewLogLine
            logger.log(
                typeLog = type,
                tag = tag,
                message = prefix + line.substring(start, end),
                useLogHack = useLogHack
            )
        }
    }

    private fun logLinesDecode(
        type: Int,
        tag: String,
        lines: Array<String>,
        logger: ILogger,
        withLineSize: Boolean,
        useLogHack: Boolean,
        logPrinter: ILogPrinter,
    ) {
        for (line: String in lines) {
            val lineLength: Int = line.length
            if (lineLength < MAXIMUM_LENGTH_LOG_A_TAG) {
                try {
                    logger.log(
                        typeLog = type,
                        tag = tag,
                        message = "| " + URLDecoder.decode(line, StandardCharsets.UTF_8.toString()),
                        useLogHack = useLogHack
                    )
                } catch (e: UnsupportedEncodingException) {
                    logPrinter.print(e)
                }
            } else {
                val maxLongSize: Int = if (withLineSize) 110 else lineLength
                for (i: Int in 0..lineLength / maxLongSize) {
                    val start = i * maxLongSize
                    var end = (i + 1) * maxLongSize
                    end = if (end > line.length) line.length else end

                    val prefix: String = if (i == 0) "| " else prefixNewLogLine
                    logger.log(
                        typeLog = type,
                        tag = tag,
                        message = prefix + line.substring(start, end),
                        useLogHack = useLogHack
                    )
                }
            }
        }
    }

    private fun logLinesDecodeForCopy(
        type: Int,
        tag: String,
        line: String,
        logger: ILogger,
        withLineSize: Boolean,
        useLogHack: Boolean
    ) {
        val lineLength = line.length
        if (lineLength < MAXIMUM_LENGTH_LOG_A_TAG) {
            try {
                logger.log(
                    typeLog = type,
                    tag = tag,
                    message = "│ " + URLDecoder.decode(line, StandardCharsets.UTF_8.toString()),
                    useLogHack = useLogHack
                )
            } catch (e: UnsupportedEncodingException) {

            }
        } else {
            val maxLongSize: Int = if (withLineSize) 110 else lineLength
            for (i: Int in 0..lineLength / maxLongSize) {
                val start = i * maxLongSize
                var end = (i + 1) * maxLongSize
                end = if (end > line.length) line.length else end

                val prefix: String = if (i == 0) "| " else prefixNewLogLine
                logger.log(
                    typeLog = type,
                    tag = tag,
                    message = prefix + line.substring(start, end),
                    useLogHack = useLogHack
                )
            }
        }
    }

    private fun bodyToString(request: Request): String? {
        return try {
            val copy: Request = request.newBuilder().build()
            val buffer = Buffer()
            val body: RequestBody? = copy.body
            if (body == null) {
                ""
            } else {
                body.writeTo(buffer)
                getJsonString(buffer.readUtf8())
            }
        } catch (e: IOException) {
            "{\"err\": \"" + e.message + "\"}"
        }
    }

    fun getJsonString(msg: String): String? {
        val message: String? = try {
            if (msg.startsWith("{")) {
                val jsonObject = JSONObject(msg)
                jsonObject.toString(/* indentSpaces = */ 3)
            } else if (msg.startsWith("[")) {
                val jsonArray = JSONArray(msg)
                jsonArray.toString(/* indentSpaces = */ 3)
            } else {
                msg
            }
        } catch (var3: JSONException) {
            msg
        } catch (var4: OutOfMemoryError) {
            OOM_OMITTED
        }
        return message
    }
}
