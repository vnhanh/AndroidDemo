package com.vnhanh.core.theme.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.vnhanh.common.compose.theme.typography.AppTypographyTokens

interface IButtonStyle {
    val textStyle: TextStyle
    val backgroundColor: Color
    val shape: Shape
}

@Immutable
data class FilledButtonStyle(
    override val textStyle: TextStyle = AppTypographyTokens.BodyMedium.copy(
        fontWeight = FontWeight.SemiBold,
    ),
    override val backgroundColor: Color,
    override val shape: Shape = CircleShape,
) : IButtonStyle

@Immutable
data class ElevatedButtonStyle(
    override val textStyle: TextStyle = AppTypographyTokens.BodyMedium.copy(
        fontWeight = FontWeight.SemiBold,
    ),
    override val backgroundColor: Color,
    override val shape: Shape = CircleShape,
    val elevation: Dp = 0.dp,
    val elevationColor: Color = Color.Unspecified,
) : IButtonStyle

@Immutable
data class OutlinedButtonStyle(
    override val textStyle: TextStyle = AppTypographyTokens.BodyMedium.copy(
        fontWeight = FontWeight.SemiBold,
    ),
    override val backgroundColor: Color,
    override val shape: Shape = CircleShape,
    val border: BorderStroke = BorderStroke(width = 1.dp, color = Color.Unspecified),
) : IButtonStyle
