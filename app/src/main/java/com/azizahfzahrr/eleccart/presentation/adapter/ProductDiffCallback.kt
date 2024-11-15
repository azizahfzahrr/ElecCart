package com.azizahfzahrr.eleccart.presentation.adapter

import androidx.recyclerview.widget.DiffUtil
import com.azizahfzahrr.eleccart.data.model.ProductDto
import com.azizahfzahrr.eleccart.data.model.ProductsResponse

class ProductDiffCallback : DiffUtil.ItemCallback<ProductDto.Data>() {
    override fun areItemsTheSame(oldItem: ProductDto.Data, newItem: ProductDto.Data): Boolean {
        return oldItem.pdId == newItem.pdId
    }

    override fun areContentsTheSame(oldItem: ProductDto.Data, newItem: ProductDto.Data): Boolean {
        return oldItem == newItem
    }
}