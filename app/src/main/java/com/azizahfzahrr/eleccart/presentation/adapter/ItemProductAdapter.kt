package com.azizahfzahrr.eleccart.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.azizahfzahrr.eleccart.databinding.ItemListProductBinding
import com.azizahfzahrr.eleccart.domain.model.Products
import com.bumptech.glide.Glide
import java.text.NumberFormat
import java.util.Locale

class ItemProductAdapter(
    private val listener: (Int) -> Unit,
) : ListAdapter<Products, ItemProductAdapter.MyViewHolder>(DiffCallback()) {

    private fun formatRupiah(amount: Int): String {
        val localeID = Locale("in", "ID")
        val formatter = NumberFormat.getNumberInstance(localeID)
        return formatter.format(amount)
    }

    class MyViewHolder(private val binding: ItemListProductBinding) : RecyclerView.ViewHolder(binding.root) {

        private val adapter = ItemProductAdapter(listener = {})

        fun bind(product: Products, listener: (Int) -> Unit) {
            binding.tvProductTitle.text = product.name

            val formattedPrice = product.price?.let { "Rp${adapter.formatRupiah(it * 15000)}" } ?: "Rp0"
            binding.tvProductPrice.text = formattedPrice

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