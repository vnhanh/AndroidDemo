package com.vnhanh.demo.authentication.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vnhanh.common.android.compose.textfield.AppTextField
import com.vnhanh.demo.authentication.R
import com.vnhanh.demo.authentication.model.LoginUiState
import com.vnhanh.demo.authentication.model.TextFieldUiData
import kotlinx.coroutines.flow.StateFlow

// Ref: https://dribbble.com/shots/18219801-Mobile-App-Login-Signup
// Ref: https://dribbble.com/shots/10749685-Login-and-Create-Account-Screen
@Composable
internal fun LoginForm(
    loginFormUiStateFlow: StateFlow<LoginUiState>,
    onEmailFieldUpdated: (TextFieldValue) -> Unit,
    onPasswordFieldUpdated: (TextFieldValue) -> Unit,
    onLoginClicked: () -> Unit,
) {
    val loginFormUiState = loginFormUiStateFlow.collectAsStateWithLifecycle().value

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        LoginField(
            fieldState = loginFormUiState.emailState,
            onFieldUpdated = onEmailFieldUpdated,
        )
        Spacer(modifier = Modifier.height(16.dp))
        LoginField(
            fieldState = loginFormUiState.passwordState,
            onFieldUpdated = onPasswordFieldUpdated,
        )
    }
}

@Composable
private fun LoginField(
    fieldState: StateFlow<TextFieldUiData>,
    onFieldUpdated: (TextFieldValue) -> Unit,
) {
    val fieldStateValue = fieldState.collectAsStateWithLifecycle().value
    AppTextField(
        value = fieldStateValue.uiValue,
        enabled = fieldStateValue.enable,
        placeHolderText = stringResource(R.string.email),
        onValueChanged = onFieldUpdated,
        keyboardType = KeyboardType.Text,
        imeAction = ImeAction.Next,
        headingComposable = {
            Icon(
                painter = painterResource(R.drawable.ic_email),
                contentDescription = stringResource(R.string.email),
                tint = MaterialTheme.colorScheme.primary,
            )
        }
    )
}
