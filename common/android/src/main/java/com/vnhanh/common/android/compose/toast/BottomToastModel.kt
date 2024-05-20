package com.vnhanh.common.android.compose.toast

import androidx.annotation.StringRes


data class BottomToastModel(
    val title: String? = null,
    @StringRes val titleResId: Int? = null,
    val message: String? = null,
    @StringRes val messageResId: Int? = null,
)
