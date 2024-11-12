package com.azizahfzahrr.eleccart.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.azizahfzahrr.eleccart.R
import com.azizahfzahrr.eleccart.data.model.ProductsResponse
import com.azizahfzahrr.eleccart.databinding.ItemListProductBinding
import com.bumptech.glide.Glide

class ProductsAdapter (
    private val onItemClick: (ProductsResponse.Product) -> Unit
) : RecyclerView.Adapter<ProductsAdapter.ProductViewHolder>() {

    private var products: List<ProductsResponse.Product> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding =
            ItemListProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = products[position]
        holder.bind(product)
    }

    override fun getItemCount(): Int = products.size

    fun submitList(newProducts: List<ProductsResponse.Product>) {
        products = newProducts
        notifyDataSetChanged()
    }

    inner class ProductViewHolder(private val binding: ItemListProductBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(product: ProductsResponse.Product) {
            binding.tvProductTitle.text = product.title
            binding.tvProductPrice.text = product.price.toString()

            Glide.with(binding.root)
                .load(product.image)
                .error(R.drawable.not_found)
                .into(binding.ivProductImage)

            binding.root.setOnClickListener {
                onItemClick(product)
            }
        }
    }
}
