package com.vnhanh.common.log

import timber.log.Timber


fun Exception.printDebugStackTrace() {
    Timber.e(this)
}

fun Throwable.printDebugStackTrace() {
    Timber.e(this)
}

fun String.printBreakLineLog(
    tag: String,
    message: String,
    maxCharsEachLine: Int = 2000,
) {
    Timber.tag(tag).d(message)
    var start = 0
    var end: Int = minOf(this.length, maxCharsEachLine)

    while (end <= this.length) {
        Timber.tag(tag).d(this.substring(start, end))
        start = end

        // if minimum == this.length + 1 => exit loop
        end = minOf(this.length + 1, start + maxCharsEachLine)
    }
}
