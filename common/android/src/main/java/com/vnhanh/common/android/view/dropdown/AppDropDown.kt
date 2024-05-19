package com.vnhanh.common.android.view.dropdown

import android.content.Context
import android.content.res.ColorStateList
import android.content.res.TypedArray
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.SpinnerAdapter
import androidx.core.content.ContextCompat
import androidx.core.view.doOnAttach
import androidx.core.widget.ImageViewCompat
import com.vnhanh.common.R
import com.vnhanh.common.data.log.printDebugStackTrace
import com.vnhanh.common.data.extensions.orZero


/**
 * TODO: Add and implement more attributes.
 */
class AppDropDownLayout : LinearLayout {
    private var contentLayoutResId: Int = -1
    private var dropDownLayoutResId: Int = -1
    private var dropDownIconResId: Int = - 1
    private var popupBackgroundDrawableResId: Int = -1

    // TODO: set max height for the popup
    // default max height of the dropdown popup is 200dp
//    private var popUpMaxHeight: Int = 200

    private var eventListener: OnEventListener? = null

    var adapter: SpinnerAdapter? = null
        set(value) {
            field = value
            selectedPosition = -1
            setupSpinner()
        }

    private var selectedPosition = -1

    /**
     * enabled to click the dropdown
     */
    var enabledClick: Boolean = true
        set(value) {
            if (field != value) {
                field = value
                getSpinner()?.doOnAttach {
                    getSpinner()?.isEnabled = field
                }
            }
        }

    constructor(context: Context) : super(context) {
        initAttributes(context, null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initAttributes(context, attrs)
    }

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr) {
        initAttributes(context, attrs)
    }

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        mode: Int
    ) : super(context, attrs, defStyleAttr, mode) {
        initAttributes(context, attrs)
    }

    private fun initAttributes(context: Context, attrs: AttributeSet?) {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.AppDropDown,
            0,
            0
        ).apply {
            try {
                contentLayoutResId = getResourceId(R.styleable.AppDropDown_contentLayout, -1)
                dropDownLayoutResId = getResourceId(R.styleable.AppDropDown_dropDownItemLayout, -1)
                popupBackgroundDrawableResId = getResourceId(R.styleable.AppDropDown_popupBackgroundDrawable, -1)
                dropDownIconResId = getResourceId(R.styleable.AppDropDown_dropDownIcon, -1)
            } catch (e: Exception) {
                e.printDebugStackTrace()
            } finally {
                recycleSafe()
            }

            inflate(context, R.layout.app_dropdown, this@AppDropDownLayout)

            setupIcon()
            setupSpinner()
        }

    }

    private fun setupIcon() {
        if (dropDownIconResId != -1) {
            // set tint color for dropdown icon
            this@AppDropDownLayout.findViewById<ImageView>(R.id.icon_dropdown)?.also { icon ->
                icon.setImageResource(dropDownIconResId)
                // TODO: set tint, size, padding, margin
//                ImageViewCompat.setImageTintList(icon, ColorStateList.valueOf(ContextCompat.getColor(context, dropDownIconResId)))
            }
        }
    }

    private fun setupSpinner() {
        val dropDownAdapter: SpinnerAdapter = adapter ?: return
        try {
            getSpinner()?.also { spinner ->
                spinner.adapter = dropDownAdapter

                if (popupBackgroundDrawableResId > -1) {
                    // set bg drawable for the popup
                    spinner.setPopupBackgroundDrawable(ContextCompat.getDrawable(context, popupBackgroundDrawableResId))
                }

                // listen to event open and close the popup to run the animation rotate dropdown icon
                spinner.setSpinnerEventsListener(object : AppSpinner.OnSpinnerEventsListener {
                    override fun onSpinnerOpened() {
                        getDropDownIcon()?.animate()?.rotationBy(-180f)?.setDuration(300)?.start()
                    }

                    override fun onSpinnerClosed() {
                        getDropDownIcon()?.animate()?.rotationBy(180f)?.setDuration(300)?.start()
                    }
                })
            }

        } catch (e: Exception) {
            e.printDebugStackTrace()
        }
    }

    private fun getSpinner() : AppSpinner? = this@AppDropDownLayout.findViewById(R.id.spinner_dropdown)

    private fun getDropDownIcon() : ImageView? = this@AppDropDownLayout.findViewById(R.id.icon_dropdown)

    /**
     * @param position: set selected item
     */
    fun setSelection(position: Int) {
        if (position in 0 until adapter?.count.orZero()) {
            eventListener?.onSelectItemAt(position)
        }
        this.findViewById<Spinner>(R.id.spinner_dropdown)?.setSelection(position)
    }

    private fun TypedArray.recycleSafe() {
        try {
            recycle()
        } catch (e: Exception) {
            // might already recycle before
            e.printDebugStackTrace()
        }
    }

    fun setEventListener(eventListener: OnEventListener) {
        this.eventListener = eventListener
    }

    interface OnEventListener {
        fun onSelectItemAt(position: Int)
    }
}
