package com.azizahfzahrr.eleccart.presentation.view.mainactivity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.azizahfzahrr.eleccart.R
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
        when (navigateTo) {
            "cart" -> replaceFragment(CartFragment(), R.id.nav_cart)
            "wishlist" -> replaceFragment(WishlistFragment(), R.id.nav_wishlist)
            else -> replaceFragment(HomeFragment(), R.id.nav_home)
        }

        binding.bottomNavigationView.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> {
                    ContextCompat.getColorStateList(this, R.color.bottom_navigation_item_color)
                    replaceFragment(HomeFragment(), R.id.nav_home)
                    true
                }

                R.id.nav_cart -> {
                    ContextCompat.getColorStateList(this, R.color.bottom_navigation_item_color)
                    replaceFragment(CartFragment(), R.id.nav_cart)
                    true
                }

                R.id.nav_wishlist -> {
                    ContextCompat.getColorStateList(this, R.color.bottom_navigation_item_color)
                    replaceFragment(WishlistFragment(), R.id.nav_wishlist)
                    true
                }

                R.id.nav_profile -> {
                    ContextCompat.getColorStateList(this, R.color.bottom_navigation_item_color)
                    replaceFragment(ProfileFragment(), R.id.nav_profile)
                    true
                }

                else -> false
            }
        }

    }

    private fun replaceFragment(fragment: Fragment, menuItemId: Int) {
        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
        if (currentFragment?.javaClass == fragment.javaClass) {
            return
        }
        binding.bottomNavigationView.menu.findItem(menuItemId).isChecked = true
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}