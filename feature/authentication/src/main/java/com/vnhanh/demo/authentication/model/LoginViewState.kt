package com.vnhanh.demo.authentication.model

import androidx.compose.ui.text.input.TextFieldValue
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class AuthUiState(
    val loginUiState: LoginUiState = LoginUiState(),
    val signUpUiState: SignUpUiState = SignUpUiState(),
    val isLoginMode: Boolean = true,
)

data class LoginUiState(
    private val _emailState: MutableStateFlow<TextFieldUiData> = MutableStateFlow(TextFieldUiData()),
    private val _passwordState: MutableStateFlow<TextFieldUiData> = MutableStateFlow(TextFieldUiData()),
    private val _keepEmailState: MutableStateFlow<Boolean> = MutableStateFlow(false),
    private val _showPasswordState: MutableStateFlow<Boolean> = MutableStateFlow(false),
) {
    val emailState = _emailState.asStateFlow()
    val passwordState = _passwordState.asStateFlow()

    fun updateEmailState(
        newState: TextFieldState = this._emailState.value.uiState,
        newEmail: TextFieldValue = this._emailState.value.uiValue,
    ) {
        _emailState.update { emailState ->
            emailState.copy(uiState = newState, uiValue = newEmail)
        }
    }

    fun updatePasswordState(
        newState: TextFieldState = this._passwordState.value.uiState,
        newPassword: TextFieldValue = this._passwordState.value.uiValue,
    ) {
        _passwordState.update { state ->
            state.copy(uiState = newState, uiValue = newPassword)
        }
    }

    fun updateKeepEmailState(keepEmail: Boolean) {
        _keepEmailState.update { keepEmail }
    }

    fun updateShowPasswordState(showPassword: Boolean) {
        _showPasswordState.update { showPassword }
    }
}

data class SignUpUiState(
    private val _emailState: MutableStateFlow<TextFieldUiData> = MutableStateFlow(TextFieldUiData()),
    private val _passwordState: MutableStateFlow<TextFieldUiData> = MutableStateFlow(TextFieldUiData()),
    private val _showPasswordState: MutableStateFlow<Boolean> = MutableStateFlow(false),
    private val _confirmPasswordState: MutableStateFlow<TextFieldUiData> = MutableStateFlow(TextFieldUiData()),
    private val _showConfirmPasswordState: MutableStateFlow<Boolean> = MutableStateFlow(false),
    private val _termsAndConditionsState: MutableStateFlow<Boolean> = MutableStateFlow(false),
//    private val _privacyPolicyState: MutableStateFlow<Boolean> = MutableStateFlow(false),
    private val _signUpButtonState: MutableStateFlow<AuthButton> = MutableStateFlow(AuthButton()),
)

enum class TextFieldState{
    ERROR,
    ENABLED,
    DISABLED,
}

data class TextFieldUiData(
    val uiState: TextFieldState = TextFieldState.ENABLED,
    val uiValue: TextFieldValue = TextFieldValue(),
    val enable: Boolean = true,
    val errorMsg: String = ""
)

enum class AuthButtonState {
    LOADING,
    ENABLED,
    DISABLED,
}

data class AuthButton(
    val state: AuthButtonState = AuthButtonState.DISABLED,
    val text: String = "",
)
