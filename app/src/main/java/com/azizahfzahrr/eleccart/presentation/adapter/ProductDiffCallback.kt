package com.azizahfzahrr.eleccart.presentation.adapter

import androidx.recyclerview.widget.DiffUtil
import com.azizahfzahrr.eleccart.data.model.ProductsResponse

class ProductDiffCallback : DiffUtil.ItemCallback<ProductsResponse.Product>() {
    override fun areItemsTheSame(oldItem: ProductsResponse.Product, newItem: ProductsResponse.Product): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ProductsResponse.Product, newItem: ProductsResponse.Product): Boolean {
        return oldItem == newItem
    }
}