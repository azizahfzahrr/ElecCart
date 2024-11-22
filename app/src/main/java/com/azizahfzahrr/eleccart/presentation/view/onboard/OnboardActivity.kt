package com.azizahfzahrr.eleccart.presentation.view.onboard

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.azizahfzahrr.eleccart.presentation.view.login.LoginActivity
import com.azizahfzahrr.eleccart.R
import com.azizahfzahrr.eleccart.databinding.ActivityOnboardBinding
import com.azizahfzahrr.eleccart.presentation.view.mainactivity.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class OnboardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOnboardBinding
    private val viewModel: OnBoardViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityOnboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ivOnboard.setImageResource(R.drawable.elec_cart)
        binding.btnGetStartedOnboard.setOnClickListener {
            lifecycleScope.launch {
                viewModel.setOnboard(true)
                val intent = Intent(this@OnboardActivity, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }
}