package com.azizahfzahrr.eleccart.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.azizahfzahrr.eleccart.data.model.Item
import com.azizahfzahrr.eleccart.databinding.ItemPaymentProductBinding
import com.bumptech.glide.Glide

class PaymentProductAdapter :
    ListAdapter<Item, PaymentProductAdapter.PaymentProductViewHolder>(PaymentProductDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentProductViewHolder {
        val binding = ItemPaymentProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PaymentProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PaymentProductViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class PaymentProductViewHolder(private val binding: ItemPaymentProductBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Item) {
            binding.tvNameProductPayment.text = item.name
            binding.tvPriceProductPayment.text = "$${item.price}"
            binding.tvAmountQtyPayment.text = item.quantity.toString()
            Glide.with(binding.root.context)
                .load(item.url)
                .into(binding.ivProductPayment)
        }
    }

    class PaymentProductDiffCallback : DiffUtil.ItemCallback<Item>() {
        override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
            return oldItem == newItem
        }
    }
}
