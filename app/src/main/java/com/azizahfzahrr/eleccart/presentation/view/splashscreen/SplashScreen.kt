package com.azizahfzahrr.eleccart.presentation.view.splashscreen

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.azizahfzahrr.eleccart.R
import com.azizahfzahrr.eleccart.databinding.ActivitySplashScreenBinding
import com.azizahfzahrr.eleccart.presentation.view.onboard.OnboardActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashScreen : AppCompatActivity() {
    private lateinit var binding: ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ivSplashScreen.setImageResource(R.drawable.elec_cart)

        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, OnboardActivity::class.java)
            startActivity(intent)
            finish()
        }, 3000)

    }
}