package com.vnhanh.common.android.compose.list

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.vnhanh.common.data.extensions.orZero


/**
 * TODO-ENHANCE: need enhance to use it
 */
@Composable
fun Modifier.simpleVerticalScrollbar(
    stateProvider: () -> LazyListState,
    width: Dp = 6.dp,
    color: Color = MaterialTheme.colorScheme.onPrimary,
): Modifier = composed {
    val targetAlpha = if (stateProvider().isScrollInProgress) 1f else 0f
    val duration = if (stateProvider().isScrollInProgress) 150 else 500

    val alpha by animateFloatAsState(
        targetValue = targetAlpha,
        animationSpec = tween(durationMillis = duration),
        label = "transform_scrollbar_list_view",
    )

    val listStateInfo by remember {
        derivedStateOf {
            LazyListInfo(
                firstVisibleElementIndex = stateProvider().layoutInfo.visibleItemsInfo.firstOrNull()?.index,
                needDrawScrollbar = stateProvider().isScrollInProgress || alpha > 0.0f,
                totalItemsCount = stateProvider().layoutInfo.totalItemsCount.coerceAtLeast(1),
                visibleItemCount = stateProvider().layoutInfo.visibleItemsInfo.size
            )
        }
    }

    drawWithContent {
        drawContent()

        // Draw scrollbar if scrolling or if the animation is still running and lazy column has content
        if (listStateInfo.needDrawScrollbar && listStateInfo.firstVisibleElementIndex != null) {
            val elementHeight = this.size.height / listStateInfo.totalItemsCount
            val scrollbarOffsetY = listStateInfo.firstVisibleElementIndex.orZero() * elementHeight
            val scrollbarHeight = listStateInfo.visibleItemCount * elementHeight

            drawRect(
                color = color,
                topLeft = Offset(this.size.width - width.toPx(), scrollbarOffsetY),
                size = Size(width.toPx(), scrollbarHeight),
                alpha = alpha
            )
        }
    }
}

data class LazyListInfo(
    val visibleItemCount: Int,
    val totalItemsCount: Int,
    val needDrawScrollbar: Boolean,
    val firstVisibleElementIndex: Int?,
)
