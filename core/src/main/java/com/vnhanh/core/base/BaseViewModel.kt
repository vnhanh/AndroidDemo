package com.vnhanh.core.base

import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel

abstract class BaseViewModel : ViewModel() {
    abstract fun showToast(
        message: String,
        @ColorRes bgColorRes: Int = 0,
        @ColorRes textColorRes: Int = 0,
        shadowDp: Int = 0,
        bottomMarginDp: Int = 0,
        showTime: Long = 0L,
    )

    abstract fun showToast(
        @StringRes msgResId: Int,
        @ColorRes bgColorRes: Int = 0,
        @ColorRes textColorRes: Int = 0,
        shadowDp: Int = 0,
        bottomMarginDp: Int = 0,
        showTime: Long = 0L,
    )

    abstract fun hideBottomToast()
}
