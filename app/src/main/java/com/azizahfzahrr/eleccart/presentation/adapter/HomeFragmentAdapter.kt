package com.azizahfzahrr.eleccart.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.azizahfzahrr.eleccart.data.model.ProductsResponse
import com.azizahfzahrr.eleccart.databinding.ItemListProductBinding
import com.bumptech.glide.Glide

class HomeFragmentAdapter(
    private val listener: OnAddToCartClickListener
) : ListAdapter<ProductsResponse.Product, HomeFragmentAdapter.ProductViewHolder>(ProductDiffCallback()) {

    interface OnAddToCartClickListener {
        fun onAddToCartClick(product: ProductsResponse.Product)
        fun onProductClick(product: ProductsResponse.Product)
    }

    inner class ProductViewHolder(private val binding: ItemListProductBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(product: ProductsResponse.Product) {
            binding.tvProductTitle.text = product.title
            binding.tvProductPrice.text = "$${product.price}"
            Glide.with(itemView.context)
                .load(product.image)
                .into(binding.ivProductImage)

            // Set click listener on product item
            binding.root.setOnClickListener {
                listener.onProductClick(product)
            }

            binding.btnAddToCart.setOnClickListener {
                listener.onAddToCartClick(product)
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