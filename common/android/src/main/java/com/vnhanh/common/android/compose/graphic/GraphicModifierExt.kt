package com.vnhanh.common.android.compose.graphic

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


/**
 * Should be aware shadow color for light and dark mode
 * @param startY: in px, startY position that drawing shadow from it.
 */
fun Modifier.topShadow(
    elevation: Dp = 4.dp,
    shadowColor: Color = Color.Black.copy(alpha = 0.1f),
    startY: Float = 0f,
) =
    this.drawWithCache {
        val shadowHeight = elevation.toPx()
        val endY = startY + shadowHeight

        val brush = Brush.verticalGradient(
            listOf(
                shadowColor,
                shadowColor.copy(0.15f),
                shadowColor.copy(0.1f),
                shadowColor.copy(0.05f),
                Color.Transparent,
            ),
            startY = startY,
            endY = endY,
            tileMode = TileMode.Decal,
        )

        this.onDrawWithContent {
            drawContent()
            drawRect(
                brush = brush,
                topLeft = Offset(0f, startY),
                size = Size(size.width, endY)
            )
        }
    }

/**
 * Should be aware shadow color for light and dark mode
 */
fun Modifier.bottomShadow(
    elevation: Dp = 4.dp,
    shadowColor: Color = Color.Black.copy(alpha = 0.1f),
) =
    this.drawWithCache {
        val shadowHeight = elevation.toPx()

        val brush = Brush.verticalGradient(
            listOf(
                shadowColor,
                Color.Transparent,
            ),
            startY = size.height,
            endY = size.height + shadowHeight,
        )

        this.onDrawBehind {
            drawRect(
                brush = brush,
                topLeft = Offset(0f, size.height),
                size = Size(size.width, shadowHeight)
            )
        }
    }
