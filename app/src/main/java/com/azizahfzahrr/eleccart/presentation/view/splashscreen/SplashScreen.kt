package com.azizahfzahrr.eleccart.presentation.view.splashscreen

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.azizahfzahrr.eleccart.presentation.view.login.LoginActivity
import com.azizahfzahrr.eleccart.presentation.view.mainactivity.MainActivity
import com.azizahfzahrr.eleccart.R
import com.azizahfzahrr.eleccart.data.source.local.PreferencedDataStore
import com.azizahfzahrr.eleccart.databinding.ActivitySplashScreenBinding
import com.azizahfzahrr.eleccart.presentation.view.onboard.OnboardActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SplashScreen : AppCompatActivity() {
    private lateinit var binding: ActivitySplashScreenBinding

    @Inject
    lateinit var preferenceDataStore: PreferencedDataStore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ivSplashScreen.setImageResource(R.drawable.elec_cart)

        lifecycleScope.launch {
            delay(1500L)
            val isLoggedIn = preferenceDataStore.isUserLoggedIn()
            val isOnboard = preferenceDataStore.getUserOnboard()

            Log.d("SplashScreen", "isLoggedIn: $isLoggedIn, isOnboard: $isOnboard")

            val nextActivity = when {
                isLoggedIn -> MainActivity::class.java
                isOnboard -> LoginActivity::class.java
                else -> OnboardActivity::class.java
            }

            val intent = Intent(this@SplashScreen, nextActivity)
            startActivity(intent)
            finish()
        }
    }
}
