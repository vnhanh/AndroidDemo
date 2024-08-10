package com.vnhanh.common.data.utils

import kotlinx.coroutines.delay


/**
 * @param block: run logic in block() until it succeeds or reach limitation of @param retryNumber
 * @return true if block() returns true, false otherwise
 */
suspend fun withRetry(
    retryNumber: Int = 3,
    delayInMilliSecondsIfFailed: Long = 100L,
    block: suspend (Int) -> Boolean
) : Boolean {
    repeat(retryNumber) { number ->
        val result = block(number)

        when {
            result -> return true
            delayInMilliSecondsIfFailed > 0 && !result && number < retryNumber - 1 -> delay(delayInMilliSecondsIfFailed)
            else -> Unit
        }
    }

    return false
}
