package com.azizahfzahrr.eleccart.data.repository

import com.azizahfzahrr.eleccart.data.model.User
import com.azizahfzahrr.eleccart.data.source.remote.FirebaseAuthDataSource
import javax.inject.Inject

interface FirebaseAuthRepository {
    fun signOut()
    fun getUserInfo(): User?
}

class FirebaseAuthRepositoryImpl @Inject constructor(
    private val firebaseAuthDataSource: FirebaseAuthDataSource
) : FirebaseAuthRepository {

    override fun signOut() {
        firebaseAuthDataSource.signOut()
    }

    override fun getUserInfo(): User? {
        return firebaseAuthDataSource.getUserInfo()
    }
}