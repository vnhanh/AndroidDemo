package com.vnhanh.network.contract

import kotlin.reflect.KClass

interface IApiServiceCreator {
    fun <T : Any> createApiService(inputClass: KClass<T>, apiUrl: String): T
}
