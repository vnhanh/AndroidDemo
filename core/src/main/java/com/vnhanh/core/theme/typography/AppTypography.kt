package com.vnhanh.core.theme.typography

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import com.vnhanh.common.compose.theme.typography.AppTypographyTokens

/**
 * Refer:
 * 1. https://www.figma.com/design/0LNtWlZj0wSn5fuPOY3Rpa/Material-3-Design-Kit-(Community)?node-id=11-1833&t=EN2sAMBS2edNw9pe-0
 * 2. https://www.figma.com/design/0LNtWlZj0wSn5fuPOY3Rpa/Material-3-Design-Kit-(Community)?node-id=49823-12141&t=EN2sAMBS2edNw9pe-0
 */

class AppTypography(
    val displayLarge: TextStyle = AppTypographyTokens.DisplayLarge,
    val displayMedium: TextStyle = AppTypographyTokens.DisplayMedium,
    val displaySmall: TextStyle = AppTypographyTokens.DisplaySmall,
    val headlineLarge: TextStyle = AppTypographyTokens.HeadlineLarge,
    val headlineMedium: TextStyle = AppTypographyTokens.HeadlineMedium,
    val headlineSmall: TextStyle = AppTypographyTokens.HeadlineSmall,
    val titleLarge: TextStyle = AppTypographyTokens.TitleLarge,
    val titleMedium: TextStyle = AppTypographyTokens.TitleMedium,
    val titleSmall: TextStyle = AppTypographyTokens.TitleSmall,
    val bodyLarge: TextStyle = AppTypographyTokens.BodyLarge,
    val bodyMedium: TextStyle = AppTypographyTokens.BodyMedium,
    val bodySmall: TextStyle = AppTypographyTokens.BodySmall,
    val labelLarge: TextStyle = AppTypographyTokens.LabelLarge,
    val labelMedium: TextStyle = AppTypographyTokens.LabelMedium,
    val labelSmall: TextStyle = AppTypographyTokens.LabelSmall,
)

val LocalAppTypography = staticCompositionLocalOf { AppTypography() }

val MaterialTheme.appTypography
    @Composable
    @ReadOnlyComposable
    get() = LocalAppTypography.current
