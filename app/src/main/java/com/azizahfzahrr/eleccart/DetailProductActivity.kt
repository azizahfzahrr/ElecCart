package com.azizahfzahrr.eleccart

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.azizahfzahrr.eleccart.data.model.ProductsResponse
import com.azizahfzahrr.eleccart.databinding.ActivityDetailProductBinding
import com.bumptech.glide.Glide

class DetailProductActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailProductBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val product = intent.getSerializableExtra("product") as? ProductsResponse.Product

        product?.let {
            displayProductDetails(it)
        }

        binding.ivLeftArrowDetailProduct.setOnClickListener {
            onBackPressed()
        }
    }

    private fun displayProductDetails(product: ProductsResponse.Product) {
        binding.tvTitleNameProductDetail.text = product.title
        binding.tvPriceProductDetail.text = "$${product.price}"
        binding.tvDescriptionFillDetailProduct.text = product.description ?: "No description available"

        Glide.with(this)
            .load(product.image)
            .error(R.drawable.not_found)
            .into(binding.ivDetailProduct)


        binding.cardWishlistDetailProduct.setOnClickListener {
            // Handle wishlist item click
        }
    }
}
