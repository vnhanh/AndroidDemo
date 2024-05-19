package com.vnhanh.common.error


import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.vnhanh.common.R
import com.vnhanh.common.modifier.singleClick.singleClick
import com.vnhanh.common.theme.color.customColorScheme
import com.vnhanh.common.theme.typography.appTypography

@Composable
fun LoadFailureAndReload(
    modifier: Modifier = Modifier,
    iconSize: Dp = 32.dp,
    title: String = stringResource(R.string.error_load_failed_title),
    titleStyle: TextStyle = MaterialTheme.appTypography.titleSmall.copy(
        color = MaterialTheme.customColorScheme.inactive,
    ),
    msg: String = stringResource(R.string.error_load_failed_message),
    msgStyle: TextStyle = MaterialTheme.appTypography.bodyMedium.copy(
        color = MaterialTheme.customColorScheme.inactive,
        textAlign = TextAlign.Center,
    ),
    reloadBtnLabel: String = stringResource(R.string.reload),
    reloadBtnStyle: TextStyle = MaterialTheme.appTypography.labelLarge.copy(
        color = MaterialTheme.colorScheme.onPrimary,
    ),
    reloadBtnBg: Color = MaterialTheme.colorScheme.primary,
    reloadBtnShape: Shape = MaterialTheme.shapes.large, // 16.dp
    @DrawableRes errorIconResId: Int? = null,
    onReload: (() -> Unit)?= null,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        if (errorIconResId != null) {
            Icon(
                modifier = Modifier.size(iconSize),
                painter = painterResource(id = errorIconResId),
                contentDescription = "Reload Icon"
            )
        } else {
            Icon(
                Icons.Default.Warning,
                modifier = Modifier.size(iconSize),
                contentDescription = "Reload Icon"
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = title,
            style = titleStyle,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = msg,
            style = msgStyle,
        )

        // Reload button
        onReload?.also { reloadAction ->
            Spacer(modifier = Modifier.height(36.dp))

            Text(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(color = reloadBtnBg, shape = reloadBtnShape)
                    .singleClick { reloadAction() }
                    .padding(16.dp),
                text = reloadBtnLabel,
                style = reloadBtnStyle
            )

        }

        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Preview
@Composable
private fun Preview() {
    LoadFailureAndReload(
        onReload = {}
    )
}
