package com.vnhanh.demo.authentication.ui

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vnhanh.authentication.domain.LoginUseCase
import com.vnhanh.authentication.model.UserData
import com.vnhanh.demo.authentication.domain.IAuthValidator
import com.vnhanh.demo.authentication.model.AuthUiState
import com.vnhanh.demo.authentication.model.TextFieldState
import com.vnhanh.network.model.ApiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val authValidator: IAuthValidator,
) : ViewModel() {
    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState = _uiState.asStateFlow()

    fun login(email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            loginUseCase.login(email = email, password = password)
                .catch { e ->
                    if (e !is CancellationException) {
                        emit(ApiState.Error(message = e.message.orEmpty()))
                    }
                }
                .collect { apiState: ApiState<UserData> ->
                    when(apiState) {
                        is ApiState.Loading -> {
                            _uiState.value.loginUiState.updateEmailState(
                                newState = TextFieldState.DISABLED
                            )
                            _uiState.value.loginUiState.updatePasswordState(
                                newState = TextFieldState.DISABLED
                            )
                        }

                        is ApiState.Error -> {

                        }

                        is ApiState.Success -> {

                        }
                    }
                }
        }
    }

    // A placeholder username validation check
    private fun isUserNameValid(username: String): Boolean {
        return if (username.contains('@')) {
            Patterns.EMAIL_ADDRESS.matcher(username).matches()
        } else {
            username.isNotBlank()
        }
    }

    // A placeholder password validation check
    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }
}