package com.vnhanh.common.android.compose.loading

import androidx.annotation.ColorRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


@Composable
fun LoadingComposable(
    @ColorRes dimColorResId: Int? = null,
    alphaDimFloat: Float = 0.65f,
    @ColorRes progressIndicatorColorResId: Int,
    progressIndicatorStrokeWidth: Dp = 4.dp,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .then(
                if (dimColorResId != null) Modifier.background(color = colorResource(id = dimColorResId).copy(alpha = alphaDimFloat)) else Modifier
            )
    ) {
        CircularProgressIndicator(
            modifier = Modifier.align(Alignment.Center),
            color = colorResource(id = progressIndicatorColorResId),
            strokeWidth = progressIndicatorStrokeWidth
        )
    }
}
