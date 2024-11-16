package com.azizahfzahrr.eleccart.presentation.view.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.azizahfzahrr.eleccart.data.model.User
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(

): ViewModel(){

    private val _user = MutableLiveData<User>()
    val user: LiveData<User> get() = _user

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    init {
        getUser()
    }

    private fun getUser() {
        val user = auth.currentUser
        if (user != null) {
            val name = user.displayName ?: "Name is not found"
            val email = user.email ?: "Email is not found"
            val phone = user.phoneNumber ?: "Phone is not found"
            val imageUrl = user.photoUrl?.toString() ?: ""

            _user.value = User(name, email, phone, imageUrl)
        } else {
            _user.value = User("User not logged in", "", "", null)
        }
    }
}