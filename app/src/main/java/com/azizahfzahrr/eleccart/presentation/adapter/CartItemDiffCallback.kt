package com.azizahfzahrr.eleccart.presentation.adapter

import androidx.recyclerview.widget.DiffUtil
import com.azizahfzahrr.eleccart.data.local.entity.CartItem

class CartItemDiffCallback : DiffUtil.ItemCallback<CartItem>() {
    override fun areItemsTheSame(oldItem: CartItem, newItem: CartItem): Boolean {
        return oldItem.productId == newItem.productId
    }

    override fun areContentsTheSame(oldItem: CartItem, newItem: CartItem): Boolean {
        return oldItem == newItem
    }
}