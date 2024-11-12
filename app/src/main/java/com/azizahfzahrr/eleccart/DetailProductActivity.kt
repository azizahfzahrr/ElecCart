package com.azizahfzahrr.eleccart

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import com.azizahfzahrr.eleccart.data.local.entity.CartItem
import com.azizahfzahrr.eleccart.data.model.ProductsResponse
import com.azizahfzahrr.eleccart.databinding.ActivityDetailProductBinding
import com.azizahfzahrr.eleccart.presentation.view.cart.CartViewModel
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailProductActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailProductBinding
    private val viewModel: CartViewModel by viewModels()

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

        binding.btnAddToCartDetailProduct.setOnClickListener {
            product?.let { addToCart(it) }
        }
    }

    private fun addToCart(product: ProductsResponse.Product) {
        val cartItem = CartItem(
            productId = product.id.toString(),
            title = product.title,
            price = product.price,
            image = product.image
        )
        viewModel.addItemToCart(cartItem)
        Toast.makeText(this, "${product.title} added to cart", Toast.LENGTH_SHORT).show()
    }
}
