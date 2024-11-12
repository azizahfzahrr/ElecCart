package com.azizahfzahrr.eleccart.data.repository

import com.azizahfzahrr.eleccart.data.model.UserRequest
import com.azizahfzahrr.eleccart.data.model.UserResponse
import com.azizahfzahrr.eleccart.data.source.remote.ApiService


class UserRepository(private val apiService: ApiService) {
    suspend fun getAllUsers(): List<UserResponse> {
        return apiService.getAllUsers()
    }

    suspend fun getUserDetail(id: Int): UserResponse {
        return apiService.getUserDetail(id)
    }

    suspend fun addUser(userRequest: UserRequest): UserResponse {
        return apiService.addUser(userRequest)
    }

    suspend fun updateUser(id: Int, userRequest: UserRequest): UserResponse {
        return apiService.updateUser(id, userRequest)
    }

    suspend fun deleteUser(id: Int): UserResponse {
        return apiService.deleteUser(id)
    }
}
