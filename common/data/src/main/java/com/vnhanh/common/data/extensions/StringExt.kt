package com.vnhanh.common.data.extensions


fun String?.toIntSafe(defaultValue: Int = 0) = this.orEmpty().toIntOrNull() ?: defaultValue

fun String?.toLongSafe(defaultValue: Long = 0L) = this.orEmpty().toLongOrNull() ?: defaultValue

fun String?.ifNullOrBlank(block: () -> String) =
    if (!this.isNullOrBlank()) this else block()

inline fun String.ifNotBlank(callBack: () -> Unit) {
    if (this.isNotBlank()) {
        callBack.invoke()
    }
}

fun String.upperCaseFirstLetter() : String {
    return when {
        this.isEmpty() -> this
        this.length == 1 -> this.uppercase()
        else -> String.format("%s%s", this.substring(0,1).uppercase(), this.substring(1))
    }
}
