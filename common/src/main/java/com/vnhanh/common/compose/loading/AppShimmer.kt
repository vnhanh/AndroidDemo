package com.vnhanh.common.compose.loading


import androidx.compose.animation.core.InfiniteTransition
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Should create shimmer that use brush and still keep created brush while not show shimmer
 * For example: Modifier.shimmerIf(visible = isLoading)
 */
fun Modifier.shimmerLoadingAnimation(): Modifier =
    composed {
        val brush: Brush? = LocalShimmerTheme.current
MaterialTheme.colorScheme
        if (brush != null) {
            this.drawBehind {
                drawRect(
                    brush = brush,
                )
            }
        } else {
            Modifier
        }
    }

@Composable
fun AppShimmer(
    widthOfShadowBrush: Int = 500,
    angleOfAxisY: Float = 270f,
    durationMillis: Int = 1000,
    label: String = "shimmer_animation",
    content: @Composable () -> Unit,
) {
    val shimmerColors: List<Color> = listOf(
        Color.White.copy(alpha = 0.3f),
        Color.White.copy(alpha = 0.5f),
        Color.White.copy(alpha = 1.0f),
        Color.White.copy(alpha = 0.5f),
        Color.White.copy(alpha = 0.3f),
    )
    val transition: InfiniteTransition = rememberInfiniteTransition(label = label)

    val translateAnimation by transition.animateFloat(
        initialValue = 0f,
        targetValue = (durationMillis + widthOfShadowBrush).toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = durationMillis,
                easing = LinearEasing,
            ),
            repeatMode = RepeatMode.Restart,
        ),
        label = "Shimmer loading animation",
    )
    val brushes = remember {
        hashMapOf<Float, Brush>()
    }

    if (!brushes.containsKey(translateAnimation)) {
        brushes[translateAnimation] = Brush.linearGradient(
            colors = shimmerColors,
            start = Offset(x = translateAnimation - widthOfShadowBrush, y = 0.0f),
            end = Offset(x = translateAnimation, y = angleOfAxisY),
        )
    }

    CompositionLocalProvider(
        LocalShimmerTheme provides brushes[translateAnimation]
    ) {
        content()
    }
}


val LocalShimmerTheme = staticCompositionLocalOf<Brush?> { null }

fun Modifier.shimmerAnimation() = this
    .background(color = Color.LightGray)
    .shimmerLoadingAnimation()

fun Modifier.shimmerAnimationWith(cornerRadius: Dp = 8.dp, bgColor: Color = Color.LightGray, isLoading: Boolean = true) = this
    .clip(RoundedCornerShape(cornerRadius))
    .background(color = bgColor, shape = RoundedCornerShape(cornerRadius))
    .then(
        if (isLoading) shimmerLoadingAnimation() else Modifier
    )

fun Modifier.shimmerAnimationWith(bgColor: Color = Color.LightGray, isLoading: Boolean = true) = this
    .background(color = bgColor)
    .then(
        if (isLoading) shimmerLoadingAnimation() else Modifier
    )

fun Modifier.shimmerAnimationWithShape(shape: Shape, bgColor: Color = Color.LightGray) = this
    .clip(shape = shape)
    .background(color = bgColor, shape = shape)
    .shimmerLoadingAnimation()
