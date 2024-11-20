package com.azizahfzahrr.eleccart.presentation.view.profile

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.azizahfzahrr.eleccart.R
import com.azizahfzahrr.eleccart.databinding.ActivityDetailProfileBinding
import com.bumptech.glide.Glide

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
        val name = intent.getStringExtra("USER_NAME") ?: getString(R.string.default_name)
        val email = intent.getStringExtra("USER_EMAIL") ?: getString(R.string.default_email)
        val phone = intent.getStringExtra("USER_PHONE") ?: getString(R.string.default_phone)
        val imageUrl = intent.getStringExtra("USER_IMAGE_URL")

        binding.etNameDetailProfile.setText(name)
        binding.etEmailDetailProfile.setText(email)
        binding.etPhoneDetailProfile.setText(phone)

        if (!imageUrl.isNullOrEmpty()) {
            Glide.with(this)
                .load(imageUrl)
                .error(R.drawable.human)
                .circleCrop()
                .into(binding.ivAvatarEditProfile)
        } else {
            binding.ivAvatarEditProfile.setImageResource(R.drawable.human)
        }
    }
}