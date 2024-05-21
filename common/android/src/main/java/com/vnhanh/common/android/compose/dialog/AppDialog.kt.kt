package com.vnhanh.common.android.compose.dialog

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.vnhanh.common.android.compose.button.ButtonUIData
import com.vnhanh.common.android.compose.gesture.singleClick.singleClick


enum class DialogOrientation {
    LEFT_RIGHT,
    TOP_BOTTOM
}

/**
 * @param leftCompose: if not null, dialog will show it. Should pass it when you want to custom the left icon.
 * @param rightCompose: if not null, dialog will show it. Should pass it when you want to custom the right icon.
 * @param iconLeft: if [leftCompose] is null, dialog will show left icon by this resource reference.
 * @param iconRight: if [rightCompose] is null, dialog will consider this param, if this one is not null, dialog will show the right icon by this resource reference.
 * @param onClickRightIcon: if it's not null, dialog will call this one when user tap on the right icon.
 * @param negativeButtonData: button data of the positive button.
 * @param positiveButtonData: button data of the negative button.
 * @param buttonsOrientation: left right or top bottom (stacked style)
 */
@Composable
fun AppDialog(
    dialogWeight: Float = 1f,
    dialogVisibleProvider: () -> Boolean = { true },
    paddingValues: PaddingValues = PaddingValues(24.dp),
    shape: Shape = RoundedCornerShape(16.dp),
    leftCompose: @Composable (() -> Unit)? = null,
    rightCompose: @Composable (() -> Unit)? = null,
    @DrawableRes iconLeft: Int? = null,
    @DrawableRes iconRight: Int? = null,
    onClickRightIcon: (() -> Unit)? = null,
    title: String = "",
    titleStyle: TextStyle = LocalTextStyle.current,
    message: String? = null,
    messageStyle: TextStyle = LocalTextStyle.current,
    isDismissOnBackPress: Boolean = true,
    isDismissOnClickOutside: Boolean = true,
    backgroundDialogColor: Color = Color.White,
    showingDialogContentAnimTime: Int = 300,
    hidingDialogContentAnimTime: Int = 200,
    buttonsOrientation: DialogOrientation = DialogOrientation.LEFT_RIGHT,
    negativeButtonData: ButtonUIData? = null,
    positiveButtonData: ButtonUIData? = null,
    onDismissListener: (() -> Unit)? = null,
) {
    DialogAnimatedVisibility(
        dialogVisibleProvider = dialogVisibleProvider,
        isDismissOnBackPress = isDismissOnBackPress,
        isDismissOnClickOutside = isDismissOnClickOutside,
        showingDialogContentAnimTime = showingDialogContentAnimTime,
        hidingDialogContentAnimTime = hidingDialogContentAnimTime,
        onDismissListener = onDismissListener,
    ) {
        DialogComposeContent(
            dialogWeight = dialogWeight,
            paddingValues = paddingValues,
            shape = shape,
            backgroundDialogColor = backgroundDialogColor,
            leftCompose = leftCompose,
            iconLeft = iconLeft,
            rightCompose = rightCompose,
            iconRight = iconRight,
            onClickRightIcon = onClickRightIcon,
            title = title,
            titleStyle = titleStyle,
            message = message,
            messageStyle = messageStyle,
            buttonsOrientation = buttonsOrientation,
            positiveBtnData = negativeButtonData,
            negativeBtnData = positiveButtonData,
            onDismissListener = onDismissListener,
        )
    }
}

@Composable
private fun DialogComposeContent(
    dialogWeight: Float = 1f,
    paddingValues: PaddingValues,
    shape: Shape,
    backgroundDialogColor: Color,
    leftCompose: @Composable (() -> Unit)?,
    iconLeft: Int?,
    rightCompose: @Composable (() -> Unit)?,
    iconRight: Int?,
    onClickRightIcon: (() -> Unit)?,
    title: String,
    titleStyle: TextStyle,
    message: String?,
    messageStyle: TextStyle,
    buttonsOrientation: DialogOrientation,
    positiveBtnData: ButtonUIData?,
    negativeBtnData: ButtonUIData?,
    onDismissListener: (() -> Unit)?
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(dialogWeight),
        shape = shape,
        colors = CardDefaults.cardColors(
            containerColor = backgroundDialogColor
        )
    ) {
        Column(modifier = Modifier.padding(paddingValues)) {
            DialogIcons(
                modifier = Modifier.fillMaxWidth(),
                leftCompose = leftCompose,
                iconLeft = iconLeft,
                rightCompose = rightCompose,
                iconRight = iconRight,
                onClickRightIcon = onClickRightIcon,
                onDismissListener = onDismissListener
            )

            Text(
                modifier = Modifier
                    .fillMaxWidth(),
                text = title,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                style = titleStyle,
            )
            if (!message.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    modifier = Modifier
                        .fillMaxWidth(),
                    text = message,
                    maxLines = 10,
                    overflow = TextOverflow.Ellipsis,
                    style = messageStyle,
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
            when (buttonsOrientation) {
                DialogOrientation.LEFT_RIGHT -> {
                    DialogHorizontalButtons(
                        leftButtonData = positiveBtnData,
                        rightButtonData = negativeBtnData,
                        onDismissListener = onDismissListener
                    )
                }

                DialogOrientation.TOP_BOTTOM -> {
                    DialogStackedButtons(
                        positiveBtnData = positiveBtnData,
                        negativeBtnData = negativeBtnData,
                        onDismissListener = onDismissListener,
                    )
                }
            }
        }
    }
}

@Composable
private fun DialogHorizontalButtons(
    leftButtonData: ButtonUIData?,
    rightButtonData: ButtonUIData?,
    onDismissListener: (() -> Unit)?
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.End
    ) {
        if (leftButtonData != null) {
            DialogComposeButton(
                modifier = Modifier
                    .then(
                        if (leftButtonData.layoutWeight > 0f) {
                            Modifier.weight(leftButtonData.layoutWeight)
                        } else {
                            Modifier
                        }
                    ),
                buttonUiData = leftButtonData,
                onDismissListener = onDismissListener,
            )
        }
        if (leftButtonData != null && rightButtonData != null) {
            Spacer(modifier = Modifier.size(8.dp))
        }
        if (rightButtonData != null) {
            DialogComposeButton(
                modifier = Modifier
                    .then(
                        if (rightButtonData.layoutWeight > 0f) {
                            Modifier.weight(rightButtonData.layoutWeight)
                        } else {
                            Modifier
                        }
                    ),
                buttonUiData = rightButtonData,
                onDismissListener = onDismissListener,
            )
        }
    }
}

@Composable
private fun DialogIcons(
    modifier: Modifier,
    leftCompose: @Composable (() -> Unit)?,
    iconLeft: Int?,
    rightCompose: @Composable (() -> Unit)?,
    iconRight: Int?,
    onClickRightIcon: (() -> Unit)?,
    onDismissListener: (() -> Unit)?,
) {
    if (leftCompose == null && iconLeft == null && rightCompose == null && iconRight == null) return

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (leftCompose != null) {
            leftCompose()
        } else if (iconLeft != null) {
            Box(modifier = Modifier.size(48.dp)) {
                Icon(
                    modifier = Modifier
                        .size(24.dp)
                        .align(Alignment.Center),
                    painter = painterResource(id = iconLeft),
                    tint = Color.Red,
                    contentDescription = "Warning icon"
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        if (rightCompose != null) {
            rightCompose()
        } else if (iconRight != null) {
            Icon(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .singleClick {
                        if (onClickRightIcon != null) {
                            onClickRightIcon()
                        } else {
                            onDismissListener?.invoke()
                        }
                    },
                painter = painterResource(id = iconRight),
                tint = Color.Unspecified,
                contentDescription = "Close Button"
            )
        }
    }
}

@Composable
private fun DialogComposeButton(
    modifier: Modifier,
    buttonUiData: ButtonUIData,
    onDismissListener: (() -> Unit)?,
) {
    Box(
        modifier
            .shadow(
                elevation = buttonUiData.elevation,
                shape = buttonUiData.shape,
                spotColor = buttonUiData.shadowSpotColor
            )
            .clip(buttonUiData.shape)
            .background(
                color = buttonUiData.btnBackgroundColor,
                shape = buttonUiData.shape
            )
            .then(
                if (buttonUiData.borderColor != null) {
                    Modifier.border(
                        width = 1.dp,
                        color = buttonUiData.borderColor,
                        shape = buttonUiData.shape
                    )
                } else {
                    Modifier
                }
            )
            .singleClick {
                buttonUiData.onClickButton?.invoke()
                onDismissListener?.invoke()
            }
            .padding(buttonUiData.paddingValues),
    ) {
        Text(
            modifier = Modifier
                .align(Alignment.Center)
                .wrapContentHeight(unbounded = true),
            text = buttonUiData.btnLabelData.text,
            color = buttonUiData.btnLabelData.color,
            style = buttonUiData.btnLabelData.style,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center,
        )
    }
}

// TODO: be careful when using, I will refactor this one when having time
@Composable
fun DialogStackedButtons(
    positiveBtnData: ButtonUIData?,
    negativeBtnData: ButtonUIData?,
    onDismissListener: (() -> Unit)? = null,
) {
    positiveBtnData?.also { btnData ->
        val titleData = btnData.btnLabelData
        val backgroundColor = btnData.btnBackgroundColor
        val borderColor = btnData.borderColor
        val onBtnClick = btnData.onClickButton
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = 8.dp,
                    start = 16.dp,
                    end = 16.dp,
                )
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {
                    onBtnClick?.invoke()
                    onDismissListener?.invoke()
                },
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(
                containerColor = backgroundColor
            ),
            border = getBorderStroke(borderColor),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 1.dp,
            )
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        vertical = 8.dp,
                        horizontal = 16.dp,
                    ),
                text = titleData.text,
                style = titleData.style,
                color = titleData.color,
            )
        }
    }
    negativeBtnData?.also { btnData ->
        val titleData = btnData.btnLabelData
        val backgroundColor = btnData.btnBackgroundColor
        val onBtnClick = btnData.onClickButton

        Spacer(modifier = Modifier.size(8.dp))
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = 8.dp,
                    start = 16.dp,
                    end = 16.dp,
                ),
            colors = ButtonDefaults.buttonColors(
                containerColor = backgroundColor
            ),
            shape = RoundedCornerShape(8.dp),
            onClick = {
                onBtnClick?.invoke()
                onDismissListener?.invoke()
            }) {
            Text(
                text = titleData.text,
                style = titleData.style,
                color = titleData.color,
            )
        }
    }
}

@Composable
private fun getBorderStroke(borderColor: Color?): BorderStroke? {
    if (borderColor == null) return null
    return BorderStroke(
        width = 1.dp,
        color = borderColor,
    )
}

