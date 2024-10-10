package com.vnhanh.authentication.model

data class User(
    val userId: String,
    val avatarUrl: String,
    val userName: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val address: String = "",
    val phoneNumber: String,
)
