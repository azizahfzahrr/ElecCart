package com.azizahfzahrr.eleccart.presentation.view.profile

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.azizahfzahrr.eleccart.R
import com.azizahfzahrr.eleccart.databinding.ActivityDetailProfileBinding
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth

class DetailProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadUserData()

        binding.ivLeftArrowDetailProfile.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

    }

    private fun loadUserData() {
        val firebaseUser = FirebaseAuth.getInstance().currentUser

        firebaseUser?.let { user ->

            val displayName = user.displayName ?: getString(R.string.default_name)
            binding.etNameDetailProfile.setText(displayName)

            val email = user.email ?: getString(R.string.default_email)
            binding.etEmailDetailProfile.setText(email)

            val phoneNumber = user.phoneNumber ?: getString(R.string.default_phone)
            binding.etPhoneDetailProfile.setText(phoneNumber)

            val photoUrlFromIntent = intent.getStringExtra("USER_PHOTO_URL")
            Log.d("DetailProfileActivity", "Photo URL from Intent: $photoUrlFromIntent")
            val photoUrl = photoUrlFromIntent ?: user.photoUrl?.toString()

            if (photoUrl != null) {
                Glide.with(this)
                    .load(photoUrl)
                    .error(R.drawable.human)
                    .circleCrop()
                    .into(binding.ivAvatarEditProfile)
            } else {
                binding.ivAvatarEditProfile.setImageResource(R.drawable.human)
            }
        } ?: run {
            binding.etNameDetailProfile.setText(getString(R.string.default_name))
            binding.etEmailDetailProfile.setText(getString(R.string.default_email))
            binding.etPhoneDetailProfile.setText(getString(R.string.default_phone))
        }
    }
}