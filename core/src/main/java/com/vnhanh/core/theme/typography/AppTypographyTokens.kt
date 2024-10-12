package com.vnhanh.common.compose.theme.typography

import androidx.compose.ui.text.TextStyle
import com.vnhanh.core.theme.typography.AppTypeScaleTokens

/**
 * Refer:
 * 1. https://m3.material.io/styles/typography/applying-type
 * 2. https://stackoverflow.com/questions/30445585/material-design-typography-headlines-titles-spacing-text-appearance
 *
 * Display: title of splash screen, onboarding screen.
 *          For display type, consider choosing a more expressive font, such as a handwritten or script style.
 * Headline: the top headline text.
 * Title: screen title, section title, sub-section title, card title, field label.
 * Body: normal text, content.
 * Label: tag, button, link, field label.
 */
internal object AppTypographyTokens {
    val BodyLarge =
        TextStyle.Default.copy(
            fontFamily = AppTypeScaleTokens.BodyLargeFont,
            fontWeight = AppTypeScaleTokens.BodyLargeWeight,
            fontSize = AppTypeScaleTokens.BodyLargeSize,
            lineHeight = AppTypeScaleTokens.BodyLargeLineHeight,
            letterSpacing = AppTypeScaleTokens.BodyLargeTracking,
        )

    val BodyMedium =
        TextStyle.Default.copy(
            fontFamily = AppTypeScaleTokens.BodyMediumFont,
            fontWeight = AppTypeScaleTokens.BodyMediumWeight,
            fontSize = AppTypeScaleTokens.BodyMediumSize,
            lineHeight = AppTypeScaleTokens.BodyMediumLineHeight,
            letterSpacing = AppTypeScaleTokens.BodyMediumTracking,
        )
    val BodySmall =
        TextStyle.Default.copy(
            fontFamily = AppTypeScaleTokens.BodySmallFont,
            fontWeight = AppTypeScaleTokens.BodySmallWeight,
            fontSize = AppTypeScaleTokens.BodySmallSize,
            lineHeight = AppTypeScaleTokens.BodySmallLineHeight,
            letterSpacing = AppTypeScaleTokens.BodySmallTracking,
        )
    val DisplayLarge =
        TextStyle.Default.copy(
            fontFamily = AppTypeScaleTokens.DisplayLargeFont,
            fontWeight = AppTypeScaleTokens.DisplayLargeWeight,
            fontSize = AppTypeScaleTokens.DisplayLargeSize,
            lineHeight = AppTypeScaleTokens.DisplayLargeLineHeight,
            letterSpacing = AppTypeScaleTokens.DisplayLargeTracking,
        )
    val DisplayMedium =
        TextStyle.Default.copy(
            fontFamily = AppTypeScaleTokens.DisplayMediumFont,
            fontWeight = AppTypeScaleTokens.DisplayMediumWeight,
            fontSize = AppTypeScaleTokens.DisplayMediumSize,
            lineHeight = AppTypeScaleTokens.DisplayMediumLineHeight,
            letterSpacing = AppTypeScaleTokens.DisplayMediumTracking,
        )
    val DisplaySmall =
        TextStyle.Default.copy(
            fontFamily = AppTypeScaleTokens.DisplaySmallFont,
            fontWeight = AppTypeScaleTokens.DisplaySmallWeight,
            fontSize = AppTypeScaleTokens.DisplaySmallSize,
            lineHeight = AppTypeScaleTokens.DisplaySmallLineHeight,
            letterSpacing = AppTypeScaleTokens.DisplaySmallTracking,
        )
    val HeadlineLarge =
        TextStyle.Default.copy(
            fontFamily = AppTypeScaleTokens.HeadlineLargeFont,
            fontWeight = AppTypeScaleTokens.HeadlineLargeWeight,
            fontSize = AppTypeScaleTokens.HeadlineLargeSize,
            lineHeight = AppTypeScaleTokens.HeadlineLargeLineHeight,
            letterSpacing = AppTypeScaleTokens.HeadlineLargeTracking,
        )
    val HeadlineMedium =
        TextStyle.Default.copy(
            fontFamily = AppTypeScaleTokens.HeadlineMediumFont,
            fontWeight = AppTypeScaleTokens.HeadlineMediumWeight,
            fontSize = AppTypeScaleTokens.HeadlineMediumSize,
            lineHeight = AppTypeScaleTokens.HeadlineMediumLineHeight,
            letterSpacing = AppTypeScaleTokens.HeadlineMediumTracking,
        )
    val HeadlineSmall =
        TextStyle.Default.copy(
            fontFamily = AppTypeScaleTokens.HeadlineSmallFont,
            fontWeight = AppTypeScaleTokens.HeadlineSmallWeight,
            fontSize = AppTypeScaleTokens.HeadlineSmallSize,
            lineHeight = AppTypeScaleTokens.HeadlineSmallLineHeight,
            letterSpacing = AppTypeScaleTokens.HeadlineSmallTracking,
        )
    val LabelLarge =
        TextStyle.Default.copy(
            fontFamily = AppTypeScaleTokens.LabelLargeFont,
            fontWeight = AppTypeScaleTokens.LabelLargeWeight,
            fontSize = AppTypeScaleTokens.LabelLargeSize,
            lineHeight = AppTypeScaleTokens.LabelLargeLineHeight,
            letterSpacing = AppTypeScaleTokens.LabelLargeTracking,
        )
    val LabelMedium =
        TextStyle.Default.copy(
            fontFamily = AppTypeScaleTokens.LabelMediumFont,
            fontWeight = AppTypeScaleTokens.LabelMediumWeight,
            fontSize = AppTypeScaleTokens.LabelMediumSize,
            lineHeight = AppTypeScaleTokens.LabelMediumLineHeight,
            letterSpacing = AppTypeScaleTokens.LabelMediumTracking,
        )
    val LabelSmall =
        TextStyle.Default.copy(
            fontFamily = AppTypeScaleTokens.LabelSmallFont,
            fontWeight = AppTypeScaleTokens.LabelSmallWeight,
            fontSize = AppTypeScaleTokens.LabelSmallSize,
            lineHeight = AppTypeScaleTokens.LabelSmallLineHeight,
            letterSpacing = AppTypeScaleTokens.LabelSmallTracking,
        )
    val TitleLarge =
        TextStyle.Default.copy(
            fontFamily = AppTypeScaleTokens.TitleLargeFont,
            fontWeight = AppTypeScaleTokens.TitleLargeWeight,
            fontSize = AppTypeScaleTokens.TitleLargeSize,
            lineHeight = AppTypeScaleTokens.TitleLargeLineHeight,
            letterSpacing = AppTypeScaleTokens.TitleLargeTracking,
        )
    val TitleMedium =
        TextStyle.Default.copy(
            fontFamily = AppTypeScaleTokens.TitleMediumFont,
            fontWeight = AppTypeScaleTokens.TitleMediumWeight,
            fontSize = AppTypeScaleTokens.TitleMediumSize,
            lineHeight = AppTypeScaleTokens.TitleMediumLineHeight,
            letterSpacing = AppTypeScaleTokens.TitleMediumTracking,
        )
    val TitleSmall =
        TextStyle.Default.copy(
            fontFamily = AppTypeScaleTokens.TitleSmallFont,
            fontWeight = AppTypeScaleTokens.TitleSmallWeight,
            fontSize = AppTypeScaleTokens.TitleSmallSize,
            lineHeight = AppTypeScaleTokens.TitleSmallLineHeight,
            letterSpacing = AppTypeScaleTokens.TitleSmallTracking,
        )
}
