package com.azizahfzahrr.eleccart

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.azizahfzahrr.eleccart.databinding.ActivityMainBinding
import com.azizahfzahrr.eleccart.presentation.view.cart.CartFragment
import com.azizahfzahrr.eleccart.presentation.view.home.HomeFragment
import com.azizahfzahrr.eleccart.presentation.view.profile.ProfileFragment
import com.azizahfzahrr.eleccart.presentation.view.wishlist.WishlistFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navigateTo = intent.getStringExtra("navigateTo")
        if (navigateTo == "cart") {
            replaceFragment(CartFragment())
        } else {
            replaceFragment(HomeFragment())
        }

        binding.bottomNavigationView.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId){
                R.id.nav_home -> {
                    replaceFragment(HomeFragment())
                    true
                }
                R.id.nav_cart -> {
                    replaceFragment(CartFragment())
                    true
                }
                R.id.nav_wishlist -> {
                    replaceFragment(WishlistFragment())
                    true
                }
                R.id.nav_profile -> {
                    replaceFragment(ProfileFragment())
                    true
                }
                else -> false
            }
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}