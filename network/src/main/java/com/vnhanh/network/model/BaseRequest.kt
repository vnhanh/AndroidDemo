package com.vnhanh.network.model

import com.google.gson.annotations.SerializedName

const val PARAM_DEVICE = "android"

open class BaseRequest(
    @SerializedName("device") val device: String = PARAM_DEVICE,
)
