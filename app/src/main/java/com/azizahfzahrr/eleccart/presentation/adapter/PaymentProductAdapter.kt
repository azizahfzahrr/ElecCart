package com.azizahfzahrr.eleccart.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.azizahfzahrr.eleccart.data.model.Item
import com.azizahfzahrr.eleccart.databinding.ItemPaymentProductOrderDetailBinding
import com.bumptech.glide.Glide
import java.text.NumberFormat
import java.util.Locale

class PaymentProductAdapter :
    ListAdapter<Item, PaymentProductAdapter.PaymentProductViewHolder>(PaymentProductDiffCallback()) {

    private fun formatRupiah(amount: Int): String {
        val localeID = Locale("in", "ID")
        val formatter = NumberFormat.getNumberInstance(localeID)
        return formatter.format(amount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentProductViewHolder {
        val binding = ItemPaymentProductOrderDetailBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PaymentProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PaymentProductViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class PaymentProductViewHolder(private val binding: ItemPaymentProductOrderDetailBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Item) {
            binding.tvNameProductPaymentOrderDetail.text = item.name

            val formattedPrice = item.price?.let { "Rp${formatRupiah(it)}" } ?: "Rp0"
            binding.tvPriceProductPaymentOrderDetail.text = formattedPrice

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