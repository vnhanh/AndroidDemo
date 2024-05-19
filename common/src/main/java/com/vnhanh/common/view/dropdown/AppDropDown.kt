package com.vnhanh.common.view.dropdown

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
import com.vnhanh.common.log.printDebugStackTrace


/**
 * TODO:
 * 1. Create view programmatically instead of inflating layout file.
 * 2. Add and implement more attributes.
 * 3. Move this custom view to the :common:androidHelper module
 */
//class AppDropDownLayout : LinearLayout {
//    private var contentLayoutResId: Int = -1
//    private var dropDownLayoutResId: Int = -1
//    private var dropDownIconTintColorResId: Int = - 1
//    private var popupBackgroundDrawableResId: Int = -1
//    private var selectedBackgroundId: Int = -1
//    private var unSelectedBackgroundId: Int = -1
//
//    // TODO: set max height for the popup
//    // default max height of the dropdown popup is 200dp
////    private var popUpMaxHeight: Int = 200
//
//    var adapter: SpinnerAdapter? = null
//        set(value) {
//            field = value
//            selectedPosition = -1
//            setupSpinner()
//        }
//
//    private var selectedPosition = -1
//
//    var enabledClick: Boolean = true
//        set(value) {
//            if (field != value) {
//                field = value
//                getSpinner()?.doOnAttach {
//                    getSpinner()?.isEnabled = field
//                }
//            }
//        }
//
//    constructor(context: Context) : super(context) {
//        initAttributes(context, null)
//    }
//
//    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
//        initAttributes(context, attrs)
//    }
//
//    constructor(
//        context: Context,
//        attrs: AttributeSet?,
//        defStyleAttr: Int
//    ) : super(context, attrs, defStyleAttr) {
//        initAttributes(context, attrs)
//    }
//
//    constructor(
//        context: Context,
//        attrs: AttributeSet?,
//        defStyleAttr: Int,
//        mode: Int
//    ) : super(context, attrs, defStyleAttr, mode) {
//        initAttributes(context, attrs)
//    }
//
//    private fun initAttributes(context: Context, attrs: AttributeSet?) {
//        context.theme.obtainStyledAttributes(
//            attrs,
//            R.styleable.AppDropDown,
//            0,
//            0
//        ).apply {
//            try {
//                // TODO-UPDATE: custom dropdown icon
//                contentLayoutResId = getResourceId(R.styleable.AppDropDown_contentLayout, -1)
//                dropDownLayoutResId = getResourceId(R.styleable.AppDropDown_dropDownItemLayout, -1)
//                dropDownIconTintColorResId = getResourceId(R.styleable.AppDropDown_dropDownTint, -1)
//                popupBackgroundDrawableResId = getResourceId(R.styleable.AppDropDown_popupBackgroundDrawable, -1)
//            } catch (e: Exception) {
//                e.printDebugStackTrace()
//            } finally {
//                recycleSafe()
//            }
//
//            inflate(context, R.layout.app_dropdown, this@AppDropDownLayout)
//
//            setupIcon()
//            setupSpinner()
//        }
//
//    }
//
//    private fun setupIcon() {
//        if (dropDownIconTintColorResId != -1) {
//            // set tint color for dropdown icon
//            this@AppDropDownLayout.findViewById<ImageView>(R.id.icon_dropdown)?.also { icon ->
//                ImageViewCompat.setImageTintList(icon, ColorStateList.valueOf(ContextCompat.getColor(context, dropDownIconTintColorResId)))
//            }
//        }
//    }
//
//    private fun setupSpinner(
//    ) {
//        val dropDownAdapter: SpinnerAdapter = adapter ?: return
//        try {
//            getSpinner()?.also { spinner ->
//                spinner.adapter = dropDownAdapter
//
//                if (selectedPosition == -1 && unSelectedBackgroundId > -1) {
//                    spinner.background = ContextCompat.getDrawable(context, unSelectedBackgroundId)
//                }
//
//                if (popupBackgroundDrawableResId > -1) {
//                    // set bg drawable for the popup
//                    spinner.setPopupBackgroundDrawable(ContextCompat.getDrawable(context, popupBackgroundDrawableResId))
//                }
//
//                // listen to event open and close the popup to run the animation rotate dropdown icon
//                spinner.setSpinnerEventsListener(object : AppSpinner.OnSpinnerEventsListener {
//                    override fun onSpinnerOpened() {
//                        getDropDownIcon()?.animate()?.rotationBy(-180f)?.setDuration(300)?.start()
//                    }
//
//                    override fun onSpinnerClosed() {
//                        getDropDownIcon()?.animate()?.rotationBy(180f)?.setDuration(300)?.start()
//                    }
//
//                })
//            }
//
//        } catch (e: Exception) {
//            e.printDebugStackTrace()
//        }
//    }
//
//    private fun getSpinner() : AppSpinner? = this@AppDropDownLayout.findViewById(R.id.spinner_dropdown)
//
//    private fun getDropDownIcon() : ImageView? = this@AppDropDownLayout.findViewById(R.id.icon_dropdown)
//
//    fun setSelection(position: Int) {
//        this.findViewById<Spinner>(R.id.spinner_dropdown)?.setSelection(position)
//    }
//
//    private fun TypedArray.recycleSafe() {
//        try {
//            recycle()
//        } catch (e: Exception) {
//            // might already recycle before
//            e.printDebugStackTrace()
//        }
//    }
//}
