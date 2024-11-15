package com.azizahfzahrr.eleccart.presentation.view.payment.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.azizahfzahrr.eleccart.data.local.entity.CartItem
import com.azizahfzahrr.eleccart.data.model.Item
import com.azizahfzahrr.eleccart.databinding.ItemPaymentProductBinding
import com.bumptech.glide.Glide

class PaymentProductAdapter(
    private var items: List<Item>
) : RecyclerView.Adapter<PaymentProductAdapter.PaymentProductViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentProductViewHolder {
        val binding = ItemPaymentProductBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return PaymentProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PaymentProductViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = items.size

    fun submitList(newItems: List<Item>){
        items = newItems
        notifyDataSetChanged()
    }

    inner class PaymentProductViewHolder(private val binding: ItemPaymentProductBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(cartItem: Item) {
            binding.tvNameProductPayment.text = cartItem.name
            binding.tvPriceProductPayment.text = "$${cartItem.price}"
            binding.tvAmountQtyPayment.text = cartItem.quantity.toString()
        }
    }
}