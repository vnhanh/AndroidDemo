package com.vnhanh.common.android.compose.animation

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.graphicsLayer


fun Modifier.animatedScale(
    isTriggerProvider: () -> Boolean,
    onEnd: (() -> Unit)? = null,
    to: Float,
    animationSpec: AnimationSpec<Float> = tween(200),
): Modifier =
    this.composed {
        val animatedScale by animateFloatAsState(
            targetValue = if (isTriggerProvider()) to else 1f,
            animationSpec = animationSpec,
            label = "scale_item",
            finishedListener = {
                if (isTriggerProvider()) onEnd?.invoke()
            }
        )

        graphicsLayer {
            scaleX = animatedScale
            scaleY = animatedScale
        }
    }
