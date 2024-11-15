package com.azizahfzahrr.eleccart.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.azizahfzahrr.eleccart.data.model.ProductDto
import com.azizahfzahrr.eleccart.data.model.ProductsResponse
import com.azizahfzahrr.eleccart.databinding.ItemListProductBinding
import com.bumptech.glide.Glide

class HomeFragmentAdapter(
    private val listener: OnAddToCartClickListener
) : ListAdapter<ProductDto.Data, HomeFragmentAdapter.ProductViewHolder>(ProductDiffCallback()) {

    interface OnAddToCartClickListener {
        fun onAddToCartClick(product: ProductDto.Data)
        fun onProductClick(product: ProductDto.Data)
    }

    inner class ProductViewHolder(private val binding: ItemListProductBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(product: ProductDto.Data) {
            binding.tvProductTitle.text = product.pdName
            binding.tvProductPrice.text = "$${product.pdPrice}"
            Glide.with(itemView.context)
                .load(product.pdImageUrl)
                .into(binding.ivProductImage)

            binding.root.setOnClickListener {
                listener.onProductClick(product)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ItemListProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}