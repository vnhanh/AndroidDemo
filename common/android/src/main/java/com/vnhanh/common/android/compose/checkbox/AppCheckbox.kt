package com.vnhanh.common.android.compose.checkbox

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.animation.graphics.res.animatedVectorResource
import androidx.compose.animation.graphics.res.rememberAnimatedVectorPainter
import androidx.compose.animation.graphics.vector.AnimatedImageVector
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.vnhanh.common.R
import com.vnhanh.common.android.compose.gesture.singleClick.singleClick

@Composable
internal fun AppCheckbox(
    modifier: Modifier = Modifier,
    animResId: Int = R.drawable.anim_vector_ic_check_24,
    bgColor: Color = MaterialTheme.colorScheme.primary,
    text: String,
    textStyle: TextStyle = MaterialTheme.typography.bodyMedium,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        AnimatedCheckbox(
            animResId = animResId,
            bgColor = bgColor,
            isSelected = isSelected,
            onClick = onClick
        )

        Text(
            text = text,
            style = textStyle,
        )
    }
}

@OptIn(ExperimentalAnimationGraphicsApi::class)
@Composable
internal fun AnimatedCheckbox(
    modifier: Modifier = Modifier,
    animResId: Int = R.drawable.anim_vector_ic_check_24,
    bgColor: Color = MaterialTheme.colorScheme.primary,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    val image = AnimatedImageVector.animatedVectorResource(id = animResId)
    var atEnd by remember { mutableStateOf(false) }

    LaunchedEffect(isSelected) {
        if (atEnd != isSelected) atEnd = isSelected
    }

    Image(
        painter = rememberAnimatedVectorPainter(animatedImageVector = image, atEnd = atEnd),
        modifier = modifier
            .composed {
                val bgColor by animateColorAsState(
                    targetValue = if (isSelected) bgColor else Color.Transparent,
                    animationSpec = tween(200),
                    label = "bg_color_animation"
                )

                background(color = bgColor)
            }
            .singleClick(
                isShowClickEffect = false
            ) {
                onClick()
            }
            .padding(2.dp),
        contentDescription = "",
    )
}
