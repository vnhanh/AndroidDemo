package com.vnhanh.common.android.view.dropdown

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatSpinner

class AppSpinner : AppCompatSpinner {
    private var isPopupOpened = false
    private var listener: OnSpinnerEventsListener? = null

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, mode: Int) : super(context, attrs, defStyleAttr, mode)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, mode: Int) : super(context, mode)

    constructor(context: Context) : super(context)

    override fun performClick(): Boolean {
        // register that the Spinner was opened so we have a status
        // indicator for the activity(which may lose focus for some other reasons)
        isPopupOpened = true
        listener?.onSpinnerOpened()
        return super.performClick()
    }

    fun setSpinnerEventsListener(onSpinnerEventsListener: OnSpinnerEventsListener?) {
        listener = onSpinnerEventsListener
    }

    private fun performClosedEvent() {
        isPopupOpened = false
        listener?.onSpinnerClosed()
    }

    override fun onWindowFocusChanged(hasWindowFocus: Boolean) {
        super.onWindowFocusChanged(hasWindowFocus)
        if (isPopupOpened && hasWindowFocus) {
            performClosedEvent()
        }
    }

    interface OnSpinnerEventsListener {
        fun onSpinnerOpened()
        fun onSpinnerClosed()
    }

    companion object {
        private const val TAG = "AppSpinner"
    }
}
