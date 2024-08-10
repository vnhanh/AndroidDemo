package com.vnhanh.common.data.extensions

import com.vnhanh.common.data.log.printDebugStackTrace
import java.io.FileOutputStream
import java.io.InputStream


fun FileOutputStream?.closeSafe() {
    this ?: return
    try {
        this.close()
    } catch (e: Exception) {
        e.printDebugStackTrace()
    }
}

fun InputStream?.closeSafe() {
    this ?: return
    try {
        this.close()
    } catch (e: Exception) {
        e.printDebugStackTrace()
    }
}
