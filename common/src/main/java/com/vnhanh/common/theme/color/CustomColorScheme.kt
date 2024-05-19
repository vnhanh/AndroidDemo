package com.vnhanh.common.theme.color

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Immutable
data class CustomColorScheme(
    val inactive: Color = Color.Unspecified,
)

val lightCustomColorScheme = CustomColorScheme(
    inactive = LightInactive,
)

val darkCustomColorScheme = CustomColorScheme(
    inactive = DarkInactive,
)

val LocalCustomColorScheme = staticCompositionLocalOf { lightCustomColorScheme }

val MaterialTheme.customColorScheme: CustomColorScheme
    @Composable
    @ReadOnlyComposable
    get() = LocalCustomColorScheme.current
