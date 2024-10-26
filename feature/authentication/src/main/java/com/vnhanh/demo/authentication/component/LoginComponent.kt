package com.vnhanh.demo.authentication.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vnhanh.common.android.compose.extension.appStringResource
import com.vnhanh.common.android.compose.gesture.singleClick.singleClick
import com.vnhanh.common.android.compose.textfield.AppTextField
import com.vnhanh.demo.authentication.R
import com.vnhanh.demo.authentication.model.AuthButton
import com.vnhanh.demo.authentication.model.LoginUiState
import com.vnhanh.demo.authentication.model.TextFieldUiData
import kotlinx.coroutines.flow.StateFlow

// Ref: https://dribbble.com/shots/18219801-Mobile-App-Login-Signup
// Ref: https://dribbble.com/shots/10749685-Login-and-Create-Account-Screen
@Composable
internal fun LoginForm(
    loginFormUiStateFlow: StateFlow<LoginUiState>,
    onEmailChanged: (TextFieldValue) -> Unit = {},
    onPasswordChanged: (TextFieldValue) -> Unit = {},
    onForgotPasswordClicked: () -> Unit = {},
    onLoginClicked: () -> Unit = {},
) {
    val loginFormUiState = loginFormUiStateFlow.collectAsStateWithLifecycle().value

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        // email
        LoginField(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            fieldState = loginFormUiState.emailState,
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next,
            onFieldUpdated = onEmailChanged,
        )
        Spacer(modifier = Modifier.height(16.dp))
        // password
        LoginField(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            fieldState = loginFormUiState.passwordState,
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done,
            onFieldUpdated = onPasswordChanged,
        )
        Spacer(modifier = Modifier.height(16.dp))
        ForgotPassword(
            modifier = Modifier.padding(horizontal = 16.dp),
            onClick = onForgotPasswordClicked,
        )

        LoginButton(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            onClick = onLoginClicked,
            buttonUiState = loginFormUiState.loginButtonState,
        )
    }
}

@Composable
private fun LoginField(
    modifier: Modifier = Modifier,
    fieldState: StateFlow<TextFieldUiData>,
    keyboardType: KeyboardType = KeyboardType.Email,
    imeAction: ImeAction = ImeAction.Next,
    onClickTrailingIcon: () -> Unit = {},
    onFieldUpdated: (TextFieldValue) -> Unit,
) {
    val fieldStateValue = fieldState.collectAsStateWithLifecycle().value

    AppTextField(
        modifier = modifier,
        value = fieldStateValue.uiValue,
        enabled = fieldStateValue.enable,
        placeHolderText = stringResource(fieldStateValue.placeholderResId),
        onValueChanged = onFieldUpdated,
        keyboardType = keyboardType,
        imeAction = imeAction,
        headingComposable = {
            Icon(
                painter = painterResource(fieldStateValue.headIconResId),
                contentDescription = stringResource(fieldStateValue.placeholderResId),
                tint = MaterialTheme.colorScheme.secondary,
            )
        },
        trailingComposable = {
            if (fieldStateValue.trailingResId != 0) {
                Icon(
                    painter = painterResource(fieldStateValue.trailingResId),
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .singleClick { onClickTrailingIcon() },
                    contentDescription = fieldStateValue.trailingDescResId.appStringResource(),
                    tint = MaterialTheme.colorScheme.secondary,
                )
            }
        }
    )
}

@Composable
private fun ForgotPassword(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Text(
        text = stringResource(R.string.for_password_btn),
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .singleClick { onClick() },
        style = MaterialTheme.typography.bodyMedium.copy(
            color = MaterialTheme.colorScheme.secondary,
        ),
    )
}

@Composable
private fun LoginButton(
    modifier: Modifier = Modifier,
    buttonUiState: StateFlow<AuthButton>,
    onClick: () -> Unit = {},
) {
    val buttonUiStateValue = buttonUiState.collectAsStateWithLifecycle().value
    AnimatedContent(
        targetState = buttonUiStateValue,
        transitionSpec = {
            fadeIn(tween(300)) + scaleIn(tween(300)) togetherWith fadeOut() + scaleOut()
        },
        label = "LoginButton",
    ) { uiState ->
        Text(
            text = stringResource(uiState.textResId),
            modifier = modifier
                .clip(RoundedCornerShape(8.dp))
                .singleClick { onClick() }
        )
    }
}
