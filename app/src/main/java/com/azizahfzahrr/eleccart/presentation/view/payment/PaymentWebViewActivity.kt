package com.azizahfzahrr.eleccart.presentation.view.payment

import android.graphics.Bitmap
import android.os.Bundle
import android.view.MenuItem
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import com.azizahfzahrr.eleccart.R
import com.azizahfzahrr.eleccart.databinding.ActivityPaymentWebViewBinding

class PaymentWebViewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPaymentWebViewBinding

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