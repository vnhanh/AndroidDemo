package com.vnhanh.demo.authentication.domain

interface IAuthValidator {
    fun isEmailValid(email: String) : Boolean

    fun isPasswordValid(password: String) : Boolean
}

class AuthValidatorImpl : IAuthValidator {
    override fun isEmailValid(email: String): Boolean {
        val emailRegex = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}$")
        return emailRegex.matches(email)
    }

    override fun isPasswordValid(password: String): Boolean {
        val passwordRegex = Regex("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@#$%^&+=])(?=\\S+$).{4,}$")
        return passwordRegex.matches(password)
    }

}
