package com.azizahfzahrr.eleccart

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.azizahfzahrr.eleccart.databinding.ActivityOnboardBinding

class OnboardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOnboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityOnboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ivOnboard.setImageResource(R.drawable.elec_cart)
        binding.btnGetStartedOnboard.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}