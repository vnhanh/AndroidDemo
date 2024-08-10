package com.vnhanh.common.data.extensions

import android.os.CountDownTimer
import com.vnhanh.common.data.log.printDebugStackTrace


/**
 * mapNotNull variant for Kotlin.ArrayList
 */
inline fun <T, R> ArrayList<T?>.mapNonNull(transform: (T?) -> R?): ArrayList<R> {
    val output = arrayListOf<R>()

    forEach { element -> transform(element)?.let { transformData -> output.add(transformData) } }

    return output
}

fun CountDownTimer?.cancelSafe() {
    try {
        this?.cancel()
    } catch (e: Exception) {
        e.printDebugStackTrace()
    }
}

fun CountDownTimer?.startSafe() {
    try {
        this?.start()
    } catch (e: Exception) {
        e.printDebugStackTrace()
    }
}
