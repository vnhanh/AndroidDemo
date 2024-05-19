package com.vnhanh.common.compose.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalContext
import com.vnhanh.common.compose.theme.color.DarkColorScheme
import com.vnhanh.common.compose.theme.color.LightColorScheme
import com.vnhanh.common.compose.theme.color.LocalCustomColorScheme
import com.vnhanh.common.compose.theme.color.darkCustomColorScheme
import com.vnhanh.common.compose.theme.color.lightCustomColorScheme
import com.vnhanh.common.compose.theme.typography.AppTypography
import com.vnhanh.common.compose.theme.typography.LocalAppTypography


@Composable
fun AppTheme(
    useDarkTheme: Boolean = isSystemInDarkTheme(),
    useDynamicColors: Boolean = true,
    content: @Composable () -> Unit,
) {
    val colorScheme: ColorScheme = when {
        useDynamicColors && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            if (useDarkTheme) dynamicDarkColorScheme(context = LocalContext.current)
            else dynamicLightColorScheme(context = LocalContext.current)
        }

        useDarkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val customColorScheme = when (useDarkTheme) {
        true -> darkCustomColorScheme
        else -> lightCustomColorScheme
    }

    CompositionLocalProvider(
        LocalCustomColorScheme provides customColorScheme,
        LocalAppTypography provides AppTypography(),
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            content = content,
        )
    }
}
