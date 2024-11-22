package com.azizahfzahrr.eleccart.presentation.view.payment

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.azizahfzahrr.eleccart.presentation.view.mainactivity.MainActivity
import com.azizahfzahrr.eleccart.data.local.entity.CartItem
import com.azizahfzahrr.eleccart.data.model.Item
import com.azizahfzahrr.eleccart.databinding.ActivityPaymentWebViewBinding
import com.azizahfzahrr.eleccart.presentation.view.cart.CartViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PaymentWebViewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPaymentWebViewBinding
    private val viewModel: CartViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentWebViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarPaymentWebview)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        val urlPayment = intent.getStringExtra("url_payment")
        if (urlPayment != null) {
            binding.webviewPayment.loadUrl(urlPayment)
        } else {
            Toast.makeText(this, "Invalid URL Payment", Toast.LENGTH_SHORT).show()
            finish()
        }
        binding.webviewPayment.settings.javaScriptEnabled = true

        binding.webviewPayment.webViewClient = object : WebViewClient(){

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                binding.pbLoadingWebviewPayment.isVisible = true
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                binding.pbLoadingWebviewPayment.isVisible = false

                if (url != null && url.contains("transaction_status=settlement")) {
                    applyPaymentSuccess()
                }
            }

            override fun onReceivedError(
                view: WebView?,
                request: WebResourceRequest?,
                error: WebResourceError?,
            ) {
                super.onReceivedError(view, request, error)
                Toast.makeText(
                    this@PaymentWebViewActivity,
                    "Please try again",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        binding.webviewPayment.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
                binding.pbLoadingWebviewPayment.progress = newProgress
            }
        }

    }

    private fun applyPaymentSuccess(){
        lifecycleScope.launch {

            Log.d("PaymentWebViewActivity", "Clearing the cart...")
            val selectedItems = intent.getSerializableExtra("product") as? ArrayList<Item>
            if (selectedItems != null) {
                viewModel.deleteItemsFromCart(selectedItems.map { cartItem ->
                    CartItem(
                        productId = cartItem.id.toString(),
                        title = cartItem.name,
                        price = cartItem.price,
                        quantity = cartItem.quantity,
                        image = cartItem.name
                    )
                })
            }

            Log.d("PaymentWebViewActivity", "Cart cleared successfully.")

            Toast.makeText(this@PaymentWebViewActivity, "Payment Success", Toast.LENGTH_SHORT).show()

            val intent = Intent(this@PaymentWebViewActivity, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}