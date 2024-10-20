package com.vnhanh.authentication.model

data class UserData(
    val userId: String,
    val avatarUrl: String,
    val userName: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val address: String = "",
    val phoneNumber: String,
)

fun LoginResponse.toUserData() : UserData? {
    val userId = this.userId
    val avatarUrl = this.avatarUrl
    val userName = this.userName
    val firstName = this.firstName
    val lastName = this.lastName
    val email = this.email

    if (userId.isNullOrBlank()) return null

    return UserData(
        userId = userId,
        avatarUrl = avatarUrl.orEmpty(),
        userName = userName.orEmpty(),
        firstName = firstName.orEmpty(),
        lastName = lastName.orEmpty(),
        email = email.orEmpty(),
        address = this.address.orEmpty(),
        phoneNumber = this.phoneNumber.orEmpty(),
    )
}
