package com.azizahfzahrr.eleccart.data.source.remote

import com.azizahfzahrr.eleccart.data.model.User
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

interface FirebaseAuthDataSource {
    fun signOut()
    fun getUserInfo(): User?
}

class FirebaseAuthDataSourceImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : FirebaseAuthDataSource {

    override fun getUserInfo(): User? {
        return firebaseAuth.currentUser?.let {
            val firstName = it.displayName?.split(" ")?.first() ?: "No Name"
            User(
                name = firstName,
                email = it.email ?: "No Email",
                phone = it.phoneNumber ?: "",
                imageUrl = it.photoUrl?.toString()
            )
        }
    }

    override fun signOut() {
        firebaseAuth.signOut()
    }
}