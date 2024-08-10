package com.vnhanh.common.android.compose.graphic.capture

import android.graphics.Bitmap
import android.view.View
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.doOnLayout
import androidx.core.view.drawToBitmap

/**
 * Used for capturing as a bitmap object and then deliver it through a callback
 * @param onCaptured the callback responsible for delivering the captured bitmap object
 * @param dpSize the size of the composable to capture (dp unit)
 * @param pxSize the size of the the composable to capture (px unit)
 * @param onReady the callback responsible for checking if the composable is ready to capture
 * @param backgroundColor the optional parameter to set the background color of the composable
 */
@Composable
fun CaptureComposable(
    modifier: Modifier = Modifier,
    dpSize : DpSize,
    pxSize: IntSize,
    backgroundColor: Color = Color.Transparent,
    onReady: () -> Boolean,
    onCaptured: (bitmap: Bitmap?) -> Unit = { _ -> },
    composable: @Composable () -> Unit
) {
    Column(
        modifier = modifier
            .alpha(0f)
            .size(0.dp, 0.dp)
            .verticalScroll(
                rememberScrollState(), enabled = false
            )
            .horizontalScroll(
                rememberScrollState(), enabled = false
            )
    ) {
        Box(modifier = Modifier.size(dpSize)) {
            AndroidView(
                factory = {
                    ComposeView(it).apply {
                        setContent {
                            Box(
                                modifier = Modifier
                                    .background(backgroundColor)
                                    .fillMaxSize()
                            ) {
                                composable()
                            }
                        }
                    }
                },
                modifier = Modifier.fillMaxSize(),
                update = { view: ComposeView ->
                    if (onReady()) {
                        view.run {
                            doOnLayout {
                                view.measure(
                                    View.MeasureSpec.makeMeasureSpec(pxSize.width, View.MeasureSpec.AT_MOST),
                                    View.MeasureSpec.makeMeasureSpec(pxSize.height, View.MeasureSpec.AT_MOST)
                                )
                                view.layout(view.left, view.top, view.measuredWidth, view.measuredHeight)

                                try {
                                    onCaptured(drawToBitmap())
                                } catch (e: Exception) {
                                    onCaptured(null)
                                    // TODO-LITE-ENHANCE: capture current screen
//                                    if (activity != null) {
//                                        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
//                                        val location = IntArray(2)
//                                        view.getLocationInWindow(location)
//
//                                        val window: Window = activity.window
//
//                                        PixelCopy.request(
//                                            /* source = */ window,
//                                            /* srcRect = */Rect(location[0], location[1], location[0] + view.width, location[1] + view.height),
//                                            /* dest = */bitmap,
//                                            /* listener = */{
//                                                if (it == PixelCopy.SUCCESS) {
//                                                    onBitmapped(bitmap)
//                                                }
//                                            },
//                                            /* listenerThread = */Handler(Looper.getMainLooper())
//                                        )
//                                    }
                                }
                            }
                        }
                    }
                }
            )
        }
    }
}
