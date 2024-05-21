package com.vnhanh.common.android.compose.dialog

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.DialogProperties


@Composable
fun DialogAnimatedVisibility(
    dialogVisibleProvider: () -> Boolean,
    isDismissOnBackPress: Boolean = true,
    isDismissOnClickOutside: Boolean = true,
    showingDialogContentAnimTime: Int,
    hidingDialogContentAnimTime: Int,
    onDismissListener: (() -> Unit)? = null,
    content: @Composable () -> Unit,
) {
    var isVisible by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(key1 = Unit) {
        isVisible = true
    }

    androidx.compose.ui.window.Dialog(
        onDismissRequest = { onDismissListener?.invoke() },
        properties = DialogProperties(
            dismissOnBackPress = isDismissOnBackPress,
            dismissOnClickOutside = isDismissOnClickOutside,
            usePlatformDefaultWidth = false
        )
    ) {
        Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            AnimatedVisibility(
                visible = if (dialogVisibleProvider()) isVisible else false,
                enter = fadeIn(animationSpec = tween(showingDialogContentAnimTime)) + scaleIn(
                    animationSpec = tween(showingDialogContentAnimTime),
                    initialScale = 0.2f
                ),
                exit = fadeOut(animationSpec = tween(hidingDialogContentAnimTime)) + scaleOut(
                    animationSpec = tween(hidingDialogContentAnimTime),
                    targetScale = 0.2f
                ),
                label = "transform_dialog"
            ) {
                content()
            }
        }
    }
}
