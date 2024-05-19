package com.vnhanh.common.compose.loading


import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.InfiniteRepeatableSpec
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun AnimatedLinearProgressIndicator(
    modifier: Modifier = Modifier,
    color: Color = Color.Transparent,
    trackColor: Color = MaterialTheme.colorScheme.primary,
    animationSpec: InfiniteRepeatableSpec<Float> = infiniteRepeatable(
        animation = tween(durationMillis = 1000, easing = FastOutLinearInEasing),
        repeatMode = RepeatMode.Restart,
    ),
) {
    val infiniteTransition = rememberInfiniteTransition(label = "infinite_transition_for_linear_loading")

    val animatedProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 100f,
        animationSpec = animationSpec,
        label = "animation_linear_progress_indicator"
    )

    LinearProgressIndicator(
        progress = { animatedProgress },
        modifier = modifier,
        color = color,
        trackColor = trackColor,
    )
}
