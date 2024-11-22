package com.azizahfzahrr.eleccart.presentation.view.detailproduct

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.azizahfzahrr.eleccart.R
import com.azizahfzahrr.eleccart.data.local.entity.CartItem
import com.azizahfzahrr.eleccart.data.model.Item
import com.azizahfzahrr.eleccart.data.model.Order
import com.azizahfzahrr.eleccart.data.model.ProductDto
import com.azizahfzahrr.eleccart.data.source.local.WishlistEntity
import com.azizahfzahrr.eleccart.databinding.ActivityDetailProductBinding
import com.azizahfzahrr.eleccart.domain.model.Products
import com.azizahfzahrr.eleccart.presentation.view.cart.CartViewModel
import com.azizahfzahrr.eleccart.presentation.view.payment.PaymentActivity
import com.azizahfzahrr.eleccart.presentation.view.wishlist.WishlistViewModel
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import java.text.NumberFormat
import java.util.Locale

@AndroidEntryPoint
class DetailProductActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailProductBinding
    private val viewModel: CartViewModel by viewModels()
    private val wishlistViewModel: WishlistViewModel by viewModels()
    private val USD_TO_IDR = 15000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val product = intent.getSerializableExtra("product") as? ProductDto.Data
        if (product == null){
            val searchedProduct = intent.getParcelableExtra<Products>("product")
            searchedProduct?.let { displaySearchedProductDetails(it) }
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
                    price = it.pdPrice?.times(USD_TO_IDR)
                )
            }
        }
    }

    private fun formatRupiah(amount: Int): String {
        val localeID = Locale("in", "ID")
        val formatter = NumberFormat.getNumberInstance(localeID)
        return formatter.format(amount)
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
        val convertedPrice = product.pdPrice?.times(USD_TO_IDR) ?: 0

        binding.tvTitleNameProductDetail.text = product.pdName
        binding.tvPriceProductDetail.text = "Rp${formatRupiah(convertedPrice)}"
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
            addToCart(product)
        }
        binding.btnBuyNowDetailProduct.setOnClickListener {
            navigateToPayment(product)
        }
    }

    private fun displaySearchedProductDetails(product: Products) {
        val convertedPrice = product.price?.times(USD_TO_IDR) ?: 0

        binding.tvTitleNameProductDetail.text = product.name
        binding.tvPriceProductDetail.text = "Rp${formatRupiah(convertedPrice)}"
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
                pdPrice = convertedPrice,
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
            addToCart(ProductDto.Data(
                pdId = product.id,
                pdName = product.name,
                pdPrice = convertedPrice,
                pdDescription = product.description,
                pdImageUrl = product.image,
                pdQuantity = product.quantity,
                totalAverageRating = product.averageRating,
                totalReviews = product.totalReviews,
                categories = null,
                pdData = null
            ))
        }
        binding.btnBuyNowDetailProduct.setOnClickListener {
            navigateToPayment(ProductDto.Data(
                pdId = product.id,
                pdName = product.name,
                pdPrice = convertedPrice,
                pdDescription = product.description,
                pdImageUrl = product.image,
                pdQuantity = product.quantity,
                totalAverageRating = product.averageRating,
                totalReviews = product.totalReviews,
                categories = null,
                pdData = null
            ))
        }
    }

    private fun navigateToPayment(product: ProductDto.Data) {
        val convertedPrice = product.pdPrice?.times(USD_TO_IDR) ?: 0

        if (product == null) {
            Toast.makeText(this, "Please select a product to proceed", Toast.LENGTH_SHORT).show()
            return
        }

        val orderItem = Item(
            id = product.pdId ?: 0,
            name = product.pdName?.take(20) ?: "",
            price = convertedPrice,
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
        val convertedPrice = product.pdPrice?.times(USD_TO_IDR) ?: 0

        val cartItem = CartItem(
            productId = product.pdId.toString(),
            title = product.pdName?.take(20),
            price = convertedPrice,
            image = product.pdImageUrl
        )
        viewModel.addItemToCart(cartItem)
        Toast.makeText(this, "${product.pdName} added to cart", Toast.LENGTH_SHORT).show()
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
                        product.pdPrice?.times(USD_TO_IDR),
                        product.pdImageUrl,
                        true
                    )
                )
                Toast.makeText(this, "${product.pdName} added to wishlist", Toast.LENGTH_SHORT)
                    .show()
            }
            updateWishlistIcon(productId)
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