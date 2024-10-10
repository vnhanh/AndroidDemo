package com.vnhanh.demo.authentication.ui

/**
 * Data validation state of the login form.
 */
data class LoginViewState(
    val usernameError: Int? = null,
    val passwordError: Int? = null,
    val isDataValid: Boolean = false
)
