package com.azizahfzahrr.eleccart.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.azizahfzahrr.eleccart.databinding.ItemListProductBinding
import com.azizahfzahrr.eleccart.domain.model.Products
import com.bumptech.glide.Glide

class ItemProductAdapter(
    private val listener: (Int) -> Unit
) : ListAdapter<Products, ItemProductAdapter.MyViewHolder>(DiffCallback()) {

    class MyViewHolder(private val binding: ItemListProductBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(product: Products, listener: (Int) -> Unit) {
            binding.tvProductTitle.text = product.name
            binding.tvProductPrice.text = "$${product.price}"

            Glide.with(binding.root.context)
                .load(product.image)
                .into(binding.ivProductImage)

            binding.root.setOnClickListener {
                product.id.let { id ->
                    listener(id)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemListProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val product = getItem(position)
        holder.bind(product, listener)
    }

    class DiffCallback : DiffUtil.ItemCallback<Products>() {
        override fun areItemsTheSame(oldItem: Products, newItem: Products): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Products, newItem: Products): Boolean {
            return oldItem == newItem
        }
    }
}