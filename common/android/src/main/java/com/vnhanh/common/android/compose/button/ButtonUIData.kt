package com.vnhanh.common.android.compose.button

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


/**
 * @param paddingValues: padding values of the button
 * @param layoutWeight: layout weight while measuring, 0f means that this button is wrap content
 */
data class ButtonUIData(
    val btnLabelData: TextUIData,
    val paddingValues: PaddingValues = PaddingValues(8.dp),
    val btnBackgroundColor: Color,
    val borderColor: Color? = null,
    val shape: Shape = RectangleShape,
    val layoutWeight: Float = 1f,
    val elevation: Dp = 0.dp,
    val shadowSpotColor: Color = Color.Transparent,
    val thresholdTimeDebounceClick: Long = 1000L,
    val onClickButton: (() -> Unit)? = null,
)


data class TextUIData(
    val text: String,
    val style: TextStyle,
    val color: Color,
    val textAlign: TextAlign = TextAlign.Start,
)
