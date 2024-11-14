package com.azizahfzahrr.eleccart.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.azizahfzahrr.eleccart.data.source.local.WishlistEntity
import com.azizahfzahrr.eleccart.databinding.ItemWishlistProductBinding
import com.azizahfzahrr.eleccart.presentation.view.wishlist.WishlistViewModel
import com.bumptech.glide.Glide
import com.azizahfzahrr.eleccart.R

class WishlistAdapter(private val wishlistViewModel: WishlistViewModel) :
    RecyclerView.Adapter<WishlistAdapter.WishlistViewHolder>() {

    var wishlistProducts: List<WishlistEntity> = listOf()

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
            binding.tvAmountProductWishlist.text = "$${product.price}"
            binding.tvTitleProductWishlist.text = product.title

            Glide.with(binding.ivProductCart.context)
                .load(product.image)
                .into(binding.ivProductCart)

            val color = if (product.isInWishlist) {
                ContextCompat.getColor(binding.root.context, R.color.wishlist_filled)
            } else {
                ContextCompat.getColor(binding.root.context, R.color.wishlist_empty)
            }

            binding.ivCartWishlist.setColorFilter(color)

            binding.ivCartWishlist.setOnClickListener {
                if (product.isInWishlist) {
                    wishlistViewModel.removeProductFromWishlist(product.productId)
                } else {
                    wishlistViewModel.addProductToWishlist(product)
                }
                product.isInWishlist = !product.isInWishlist
                notifyItemChanged(adapterPosition)
            }
        }
    }
}