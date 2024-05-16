package com.vnhanh.common.textfield

import androidx.annotation.IntRange
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActionScope
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.vnhanh.common.theme.AppTypography.fontSize13LineHeight18Medium

/**
 * @param onKeyBoardAction: will replace for [onKeyBoardNextAction] and [onKeyBoardDoneAction]
 * @param enabled - controls the enabled state of the field. When false, the text field will be neither editable nor focusable, the input of the text field will not be selectable
 * @param readOnly - controls the editable state of the field. When true, the text field can not be modified, however, a user can focus it and copy text from it. Read-only text fields are usually used to display pre-filled forms that user can not edit
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTextField(
    modifier: Modifier = Modifier,
    value: TextFieldValue,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    placeHolderText: String? = null,
    placeHolderColor: Color = MaterialTheme.colorScheme.onSurface,
    placeHolderTextAlign: TextAlign = TextAlign.Start,
    shape: Shape = RoundedCornerShape(8.dp),
    paddingValues: PaddingValues = PaddingValues(vertical = 8.dp, horizontal = 16.dp),
    onValueChanged: (TextFieldValue) -> Unit,
    keyboardType: KeyboardType = KeyboardType.Number,
    imeAction: ImeAction = ImeAction.Next,
    onKeyBoardNextAction: (KeyboardActionScope.() -> Unit)? = null,
    onKeyBoardDoneAction: (KeyboardActionScope.() -> Unit)? = null,
    onKeyBoardAction: KeyboardActions? = null,
    focusColor: Color = Color.Black,
    unFocusColor: Color = MaterialTheme.colorScheme.onSurface,
    cursorColor: Color = Color.Black,
    textColor: Color = Color.Black,
    textStyle: TextStyle = fontSize13LineHeight18Medium,
    textAlign: TextAlign? = null,
    @IntRange(from = 1) maxLines: Int = 1,
    headingComposable: (@Composable () -> Unit)? = null,
    trailingComposable: (@Composable () -> Unit)? = null,
) {
    // make edit text hide placeholder when focusing
    var shouldShowPlaceHolder by remember(placeHolderText) {
        mutableStateOf(placeHolderText.isNullOrEmpty().not())
    }
    val interactionSource = remember { MutableInteractionSource() }
    val customTextSelectionColors = TextSelectionColors(
        // change thumb color
        handleColor = textColor,
        // change background color while selecting
        backgroundColor = Color.Gray,
    )

    CompositionLocalProvider(LocalTextSelectionColors provides customTextSelectionColors) {
        BasicTextField(
            modifier = modifier.onFocusChanged { focusState ->
                shouldShowPlaceHolder = !focusState.isFocused
            },
            value = value,
            enabled = enabled,
            readOnly = readOnly,
            textStyle = textStyle.merge(
                color = textColor,
                textAlign = textAlign ?: TextAlign.Unspecified,
            ),
            onValueChange = onValueChanged,
            maxLines = maxLines,
            keyboardOptions = KeyboardOptions(
                keyboardType = keyboardType,
                imeAction = imeAction,
            ),
            keyboardActions = onKeyBoardAction ?: KeyboardActions(
                onNext = onKeyBoardNextAction,
                onDone = onKeyBoardDoneAction
            ),
            cursorBrush = SolidColor(cursorColor),
            decorationBox = { innerTextField ->
                TextFieldDefaults.DecorationBox(
                    innerTextField = innerTextField,
                    placeholder =
                    if (shouldShowPlaceHolder) {
                        @Composable {
                            Text(
                                text = placeHolderText.orEmpty(),
                                style = textStyle.merge(
                                    color = placeHolderColor,
                                    textAlign = placeHolderTextAlign
                                )
                            )
                        }
                    } else {
                        null
                    },
                    interactionSource = interactionSource,
                    enabled = true,
                    singleLine = maxLines == 1,
                    value = value.text,
                    visualTransformation = VisualTransformation.None,
                    contentPadding = paddingValues,
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = unFocusColor,
                        focusedBorderColor = focusColor,
                        cursorColor = cursorColor,
                    ),
                    leadingIcon = headingComposable,
                    trailingIcon =
                    if (trailingComposable != null) {
                        {
                            trailingComposable()
                        }
                    } else {
                        null
                    },
                    container = {
                        OutlinedTextFieldDefaults.ContainerBox(
                            enabled = true,
                            isError = false,
                            interactionSource = interactionSource,
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedBorderColor = unFocusColor,
                                focusedBorderColor = focusColor,
                                cursorColor = cursorColor,
                            ),
                            shape = shape,
                        )
                    }
                )
            }
        )
    }
}
