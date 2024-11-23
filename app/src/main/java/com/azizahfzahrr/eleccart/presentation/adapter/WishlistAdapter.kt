package com.azizahfzahrr.eleccart.presentation.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.azizahfzahrr.eleccart.data.source.local.WishlistEntity
import com.azizahfzahrr.eleccart.databinding.ItemWishlistProductBinding
import com.azizahfzahrr.eleccart.presentation.view.wishlist.WishlistViewModel
import com.bumptech.glide.Glide
import com.azizahfzahrr.eleccart.R
import com.azizahfzahrr.eleccart.data.model.ProductDto
import com.azizahfzahrr.eleccart.presentation.listener.ItemWishlistListener
import com.azizahfzahrr.eleccart.presentation.view.detailproduct.DetailProductActivity
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

class WishlistAdapter(
    private val wishlistViewModel: WishlistViewModel,
    private val itemWishlistListener: ItemWishlistListener
) : RecyclerView.Adapter<WishlistAdapter.WishlistViewHolder>() {

    var wishlistProducts: List<WishlistEntity> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WishlistViewHolder {
        val binding = ItemWishlistProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WishlistViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WishlistViewHolder, position: Int) {
        val product = wishlistProducts[position]
        holder.bind(product)
    }

    override fun getItemCount(): Int = wishlistProducts.size

    inner class WishlistViewHolder(private val binding: ItemWishlistProductBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(product: WishlistEntity) {
            binding.tvAmountProductWishlist.text = formatPrice(product.price?.toDouble() ?: 0.0)
            binding.tvTitleProductWishlist.text = product.title

            Glide.with(binding.ivProductCart.context)
                .load(product.image)
                .into(binding.ivProductCart)

            binding.cardViewCartWishlist.setOnClickListener {
                itemWishlistListener.onAddToCartClicked(product)
            }

            binding.cardViewDeleteWishlist.setOnClickListener {
                itemWishlistListener.onDeleteFromWishlistClicked(product.productId)
            }
        }

        private fun formatPrice(price: Double): String {
            val symbols = DecimalFormatSymbols(Locale("id", "ID"))
            symbols.groupingSeparator = '.'
            val format = DecimalFormat("#,###", symbols)
            return "Rp${format.format(price)}"
        }
    }
}