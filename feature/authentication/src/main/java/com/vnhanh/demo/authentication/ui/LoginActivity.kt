package com.vnhanh.demo.authentication.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import com.vnhanh.demo.authentication.component.AuthScreenTopBar
import com.vnhanh.demo.authentication.component.LoginForm

class LoginActivity : AppCompatActivity() {

    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ComposeView(this).apply {
            setContent {
                setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
                LoginScreen()
            }
        }
    }

    @Composable
    private fun LoginScreen() {
        Column(modifier = Modifier.fillMaxSize()) {
            AuthScreenTopBar()
            LoginForm(
                loginFormUiStateFlow = loginViewModel.uiState,
                onEmailChanged = { fieldValue ->
                    loginViewModel.onEmailUpdated(fieldValue)
                },
                onPasswordChanged = { fieldValue ->
                    loginViewModel.onEmailUpdated(fieldValue)
                },
                onForgotPasswordClicked = {
                    loginViewModel.onForgotPasswordClicked()
                },
                onLoginClicked = {
                    loginViewModel.login()
                }
            )
        }
    }
}
