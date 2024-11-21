package com.azizahfzahrr.eleccart.presentation.view.detailproduct

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.azizahfzahrr.eleccart.MainActivity
import com.azizahfzahrr.eleccart.R
import com.azizahfzahrr.eleccart.data.local.entity.CartItem
import com.azizahfzahrr.eleccart.data.model.Item
import com.azizahfzahrr.eleccart.data.model.Order
import com.azizahfzahrr.eleccart.data.model.ProductDto
import com.azizahfzahrr.eleccart.data.model.ProductsResponse
import com.azizahfzahrr.eleccart.data.source.local.WishlistEntity
import com.azizahfzahrr.eleccart.databinding.ActivityDetailProductBinding
import com.azizahfzahrr.eleccart.domain.model.Products
import com.azizahfzahrr.eleccart.presentation.view.cart.CartFragment
import com.azizahfzahrr.eleccart.presentation.view.cart.CartViewModel
import com.azizahfzahrr.eleccart.presentation.view.payment.PaymentActivity
import com.azizahfzahrr.eleccart.presentation.view.wishlist.WishlistViewModel
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailProductActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailProductBinding
    private val viewModel: CartViewModel by viewModels()
    private val wishlistViewModel: WishlistViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var searchedProduct: Products?
        binding = ActivityDetailProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val product = intent.getSerializableExtra("product") as? ProductDto.Data
        if (product == null){
            searchedProduct = intent.getParcelableExtra("product")
            if (searchedProduct != null) {
                displaySearchedProductDetails(searchedProduct)
            }
        } else {
            displayProductDetails(product)
        }

        binding.ivLeftArrowDetailProduct.setOnClickListener {
            onBackPressed()
        }

        binding.ivShareDetailProduct.setOnClickListener {
            product?.let {
                shareToAllPlatforms(
                    context = this,
                    title = it.pdName,
                    description = it.pdDescription,
                    imageUrl = it.pdImageUrl,
                    price = it.pdPrice
                )
            }
        }


    }

    private fun shareToAllPlatforms(context: DetailProductActivity, title: String?, description: String?, imageUrl: String?, price: Int?) {
        val shareText = buildString {
            append("Check out this product!\n")
            append("Name: $title\n")
            price?.let { append("Price: $$it\n") }
            description?.let { append("Description: $description\n") }
            imageUrl?.let { append("Image: $it\n") }
        }

        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, shareText)
        }

        val chooser = Intent.createChooser(shareIntent, "Share product via")
        context.startActivity(chooser)
    }


    private fun displayProductDetails(product: ProductDto.Data) {
        binding.tvTitleNameProductDetail.text = product.pdName
        binding.tvPriceProductDetail.text = "$${product.pdPrice}"
        binding.tvDescriptionFillDetailProduct.text = product.pdDescription ?: "No description available"

        Glide.with(this)
            .load(product.pdImageUrl)
            .error(R.drawable.not_found)
            .into(binding.ivDetailProduct)

        updateWishlistIcon(product.pdId?.toString())

        binding.cardWishlistDetailProduct.setOnClickListener {
            toggleWishlist(product)
        }
        binding.btnAddToCartDetailProduct.setOnClickListener {
            product?.let { addToCart(it) }
        }
        binding.btnBuyNowDetailProduct.setOnClickListener {
            product?.let { navigateToPayment(it) }
        }
    }

    private fun displaySearchedProductDetails(product: Products) {
        binding.tvTitleNameProductDetail.text = product.name
        binding.tvPriceProductDetail.text = "$${product.price}"
        binding.tvDescriptionFillDetailProduct.text = product.description ?: "No description available"

        Glide.with(this)
            .load(product.image)
            .error(R.drawable.not_found)
            .into(binding.ivDetailProduct)

        updateWishlistIcon(product.id?.toString())

        binding.cardWishlistDetailProduct.setOnClickListener {
            toggleWishlist(ProductDto.Data(
                pdId = product.id,
                pdName = product.name,
                pdPrice = product.price,
                pdDescription = product.description,
                pdImageUrl = product.image,
                pdQuantity = product.quantity,
                totalAverageRating = product.averageRating,
                totalReviews = product.totalReviews,
                categories = null,
                pdData = null
            ))
        }
        binding.btnAddToCartDetailProduct.setOnClickListener {
            product?.let { addToCart(ProductDto.Data(
                pdId = product.id,
                pdName = product.name,
                pdPrice = product.price,
                pdDescription = product.description,
                pdImageUrl = product.image,
                pdQuantity = product.quantity,
                totalAverageRating = product.averageRating,
                totalReviews = product.totalReviews,
                categories = null,
                pdData = null
            )) }
        }
        binding.btnBuyNowDetailProduct.setOnClickListener {
            product?.let { navigateToPayment(ProductDto.Data(
                pdId = product.id,
                pdName = product.name,
                pdPrice = product.price,
                pdDescription = product.description,
                pdImageUrl = product.image,
                pdQuantity = product.quantity,
                totalAverageRating = product.averageRating,
                totalReviews = product.totalReviews,
                categories = null,
                pdData = null
            )) }
        }
    }

    private fun navigateToPayment(product: ProductDto.Data) {
        if (product == null) {
            Toast.makeText(this, "Please select a product to proceed", Toast.LENGTH_SHORT).show()
            return
        }

        val orderItem = Item(
            id = product.pdId ?: 0,
            name = product.pdName?.take(20) ?: "",
            price = product.pdPrice ?: 0,
            url = product.pdImageUrl ?: "",
            quantity = 1
        )

        val totalAmount = product.pdPrice ?: 0

        val orderRequest = Order(
            amount = totalAmount,
            email = "user@example.com",
            items = listOf(orderItem)
        )

        val intent = Intent(this, PaymentActivity::class.java)
        intent.putExtra("order_request", orderRequest)
        startActivity(intent)
    }

    private fun addToCart(product: ProductDto.Data) {
        val cartItem = CartItem(
            productId = product.pdId.toString(),
            title = product.pdName?.take(20),
            price = product.pdPrice,
            image = product.pdImageUrl
        )
        viewModel.addItemToCart(cartItem)
        Toast.makeText(this, "${product.pdName} added to cart", Toast.LENGTH_SHORT).show()
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("navigateTo", "cart")
        startActivity(intent)
    }

    private fun toggleWishlist(product: ProductDto.Data) {
        val productId = product.pdId.toString()

        wishlistViewModel.isInWishlist(productId).observe(this) { isInWishlist ->
            if (isInWishlist) {
                wishlistViewModel.removeProductFromWishlist(productId)
                Toast.makeText(this, "${product.pdName} removed from wishlist", Toast.LENGTH_SHORT)
                    .show()
            } else {
                wishlistViewModel.addProductToWishlist(
                    WishlistEntity(
                        productId ?: "",
                        product.pdName,
                        product.pdPrice,
                        product.pdImageUrl,
                        true
                    )
                )
                Toast.makeText(this, "${product.pdName} added to wishlist", Toast.LENGTH_SHORT)
                    .show()
            }
            updateWishlistIcon(productId)

            val intent = Intent(this, MainActivity::class.java).apply {
                putExtra("navigateTo", "wishlist")
            }
            startActivity(intent)
        }
    }


    private fun updateWishlistIcon(productId: String?) {
        if (productId != null) {
            wishlistViewModel.isInWishlist(productId).observe(this) { isInWishlist ->
                val color = if (isInWishlist) R.color.wishlist_filled else R.color.wishlist_empty
                binding.wishlistDetailProduct.setColorFilter(ContextCompat.getColor(this, color))
            }
        }
    }
}