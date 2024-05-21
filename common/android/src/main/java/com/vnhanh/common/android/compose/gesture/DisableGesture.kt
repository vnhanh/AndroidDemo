package com.vnhanh.common.android.compose.gesture

import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.pointerInput


/**
 * TODO: will check and refactor awaitPointerEventScope when I have time
 * Disable touch events upon certain condition is satisfied
 * @param disabledProvider: disable touch events
 */
fun Modifier.disableTouch(disabledProvider: () -> Boolean): Modifier {
    return if (disabledProvider()) {
        pointerInput(Unit) {
            awaitPointerEventScope {
                // we should wait for all new pointer events
                while (true) {
                    awaitPointerEvent(pass = PointerEventPass.Initial)
                        .changes
                        .forEach(PointerInputChange::consume)
                }
            }
        }
    } else {
        this
    }
}
