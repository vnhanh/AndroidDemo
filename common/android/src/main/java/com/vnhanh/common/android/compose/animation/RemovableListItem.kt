package com.vnhanh.common.android.compose.animation

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp


fun Modifier.transitionRemoveItemFromList(
    isVisibleProvider: () -> Boolean,
    animationTime: Int
) = composed {
    val configuration = LocalConfiguration.current

    // slide out to left side
    val offsetX by animateDpAsState(
        targetValue = if (isVisibleProvider()) 0.dp else -configuration.screenWidthDp.dp,
        animationSpec = tween(animationTime),
        label = "remove_item_set",
    )

    if (isVisibleProvider()) {
        this
    } else {
        this.offset(x = offsetX)
    }
}
