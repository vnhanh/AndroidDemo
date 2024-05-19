package com.vnhanh.common.view.dropdown

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatSpinner

class AppSpinner : AppCompatSpinner {
    private var listener: OnSpinnerEventsListener? = null
    private var isOpenInitiated = false

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, mode: Int) : super(context, attrs, defStyleAttr, mode)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, mode: Int) : super(context, mode)

    constructor(context: Context) : super(context)

    override fun performClick(): Boolean {
        // register that the Spinner was opened so we have a status
        // indicator for the activity(which may lose focus for some other reasons)
        isOpenInitiated = true
        listener?.onSpinnerOpened()
        return super.performClick()
    }

    fun setSpinnerEventsListener(onSpinnerEventsListener: OnSpinnerEventsListener?) {
        listener = onSpinnerEventsListener
    }

    private fun performClosedEvent() {
        isOpenInitiated = false
        listener?.onSpinnerClosed()
    }

    private fun hasBeenOpened(): Boolean {
        return isOpenInitiated
    }

    override fun onWindowFocusChanged(hasWindowFocus: Boolean) {
        super.onWindowFocusChanged(hasWindowFocus)
        if (hasBeenOpened() && hasWindowFocus) {
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
