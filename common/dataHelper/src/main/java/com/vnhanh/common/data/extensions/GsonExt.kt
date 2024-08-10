package com.vnhanh.common.data.extensions

import com.google.gson.Gson
import com.google.gson.JsonElement
import com.vnhanh.common.data.log.printDebugStackTrace
import java.lang.reflect.Type


fun <T> Gson.fromJsonOrNull(json: String?, clazz: Class<T>) =
    try {
        if (json != null) this.fromJson(json, clazz) else null
    } catch (e: Exception) {
        e.printDebugStackTrace()
        null
    }

inline fun <reified T> Gson.fromJsonOrNull(json: String?, type: Type): T? =
    try {
        if (json != null) this.fromJson<T>(json, type) else null
    } catch (e: Exception) {
        e.printDebugStackTrace()
        null
    }

fun <T> Gson.fromJsonOrNull(json: JsonElement?, clazz: Class<T>) =
    try {
        if (json != null) this.fromJson(json, clazz) else null
    } catch (e: Exception) {
        e.printDebugStackTrace()
        null
    }

fun JsonElement?.asJsonObjectOrNull() =
    try {
        this?.asJsonObject
    } catch (e: Exception) {
        e.printDebugStackTrace()
        null
    }
