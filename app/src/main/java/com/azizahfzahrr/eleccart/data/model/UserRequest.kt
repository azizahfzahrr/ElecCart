package com.azizahfzahrr.eleccart.data.model

data class UserRequest(
    val email: String,
    val username: String,
    val password: String,
    val name: UserResponse.User.Name,
    val address: UserResponse.User.Address,
    val phone: String
)