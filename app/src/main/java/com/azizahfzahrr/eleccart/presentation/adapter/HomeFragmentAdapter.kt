package com.azizahfzahrr.eleccart.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.azizahfzahrr.eleccart.data.model.ProductDto
import com.azizahfzahrr.eleccart.data.model.ProductsResponse
import com.azizahfzahrr.eleccart.databinding.ItemListProductBinding
import com.bumptech.glide.Glide
import java.text.NumberFormat
import java.util.Locale

class HomeFragmentAdapter(
    private val listener: OnAddToCartClickListener
) : ListAdapter<ProductDto.Data, HomeFragmentAdapter.ProductViewHolder>(ProductDiffCallback()) {

    interface OnAddToCartClickListener {
        fun onAddToCartClick(product: ProductDto.Data)
        fun onProductClick(product: ProductDto.Data)
    }

    inner class ProductViewHolder(private val binding: ItemListProductBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private val USD_TO_IDR = 15000

        fun bind(product: ProductDto.Data) {
            binding.tvProductTitle.text = product.pdName

            val priceInIDR = product.pdPrice?.times(USD_TO_IDR)
            val formattedPrice = NumberFormat.getCurrencyInstance(Locale("in", "ID")).apply { maximumFractionDigits = 0 }.format(priceInIDR)
            binding.tvProductPrice.text = formattedPrice

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