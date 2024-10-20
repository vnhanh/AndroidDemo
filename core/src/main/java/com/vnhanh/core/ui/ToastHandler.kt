package com.vnhanh.core.ui

import android.content.Context
import android.os.Parcelable
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.android.parcel.Parcelize
import javax.inject.Named

interface ToastDisplayer {
    fun showToast(
        message: String,
        @ColorRes bgColorRes: Int = 0,
        @ColorRes textColorRes: Int = 0,
        shadowDp: Int = 0,
        bottomMarginDp: Int = 0,
        showTime: Long = 0L,
    )

    fun showToast(
        @StringRes msgResId: Int,
        @ColorRes bgColorRes: Int = 0,
        @ColorRes textColorRes: Int = 0,
        shadowDp: Int = 0,
        bottomMarginDp: Int = 0,
        showTime: Long = 0L,
    )

    fun hideBottomToast()
}

/**
 * @param bottomMarginDp if not passed or this value is 0, the app might replace it by a certain bottom margin
 */
@Parcelize
data class ToastUiData(
    val message: String = "",
    val bgColorRes: Int = 0,
    val textColorRes: Int = 0,
    val shadowDp: Int = 0,
    val bottomMarginDp: Int = 0,
    val showTime: Long = 0L,
) : Parcelable


const val NAMED_SAVED_STATE_HANDLED = "TOAST_SAVED_STATE_HANDLE"
const val KEY_APP_BOTTOM_TOAST = "KEY_APP_BOTTOM_TOAST"

class ToastDisplayerImpl(
    @ApplicationContext private val appContext: Context,
    @Named(NAMED_SAVED_STATE_HANDLED) private val savedStateHandle: SavedStateHandle,
): ToastDisplayer {
    override fun showToast(
        message: String,
        @ColorRes bgColorRes: Int,
        @ColorRes textColorRes: Int,
        shadowDp: Int,
        bottomMarginDp: Int,
        showTime: Long,
    ) {
        savedStateHandle[KEY_APP_BOTTOM_TOAST] = ToastUiData(
            message = message,
            bgColorRes = bgColorRes,
            textColorRes = textColorRes,
            shadowDp = shadowDp,
            bottomMarginDp = bottomMarginDp,
            showTime = showTime,
        )
    }

    override fun showToast(
        @StringRes msgResId: Int,
        @ColorRes bgColorRes: Int,
        @ColorRes textColorRes: Int,
        shadowDp: Int,
        bottomMarginDp: Int,
        showTime: Long,
    ) {
        if (msgResId == 0) return
        savedStateHandle[KEY_APP_BOTTOM_TOAST] = ToastUiData(
            message = appContext.getString(msgResId),
            bgColorRes = bgColorRes,
            textColorRes = textColorRes,
            shadowDp = shadowDp,
            bottomMarginDp = bottomMarginDp,
            showTime = showTime,
        )
    }

    override fun hideBottomToast() {
        savedStateHandle[KEY_APP_BOTTOM_TOAST] = null
    }
}
