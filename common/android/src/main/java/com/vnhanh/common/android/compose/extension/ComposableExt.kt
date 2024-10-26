package com.vnhanh.common.android.compose.extension

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource


@Composable
fun Int?.appStringResource() : String =
    this?.takeIf { it != 0 }?.let { stringResource(it) }.orEmpty()
