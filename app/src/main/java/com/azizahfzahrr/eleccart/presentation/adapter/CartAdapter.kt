package com.azizahfzahrr.eleccart.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.azizahfzahrr.eleccart.data.local.entity.CartItem
import com.azizahfzahrr.eleccart.databinding.ItemCartProductBinding
import com.bumptech.glide.Glide

class CartAdapter(
    private var products: List<CartItem>,
    private val onRemoveClick: (CartItem) -> Unit,
    private val onQuantityChange: (CartItem, Int) -> Unit,
    private val onCheckboxClick: (CartItem) -> Unit
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    inner class CartViewHolder(private val binding: ItemCartProductBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(product: CartItem) {
            binding.tvNameProductCart.text = product.title
            binding.tvPriceProductCart.text = "$${product.price?.let { "%.2f".format(it.toDouble()) }}"
            binding.tvFillAmountProductCart.text = product.quantity?.toString() ?: "1"

            Glide.with(binding.ivProductCart.context)
                .load(product.image)
                .into(binding.ivProductCart)

            binding.checkboxProductCart.isChecked = product.isSelected
            binding.checkboxProductCart.setOnCheckedChangeListener { _, isChecked ->
                onCheckboxClick(product.copy(isSelected = isChecked))
            }

            binding.cardviewDeleteCart.setOnClickListener {
                onRemoveClick(product)
            }

            binding.cardviewMinusCart.setOnClickListener {
                onQuantityChange(product, -1)
            }

            binding.cardviewPlusCart.setOnClickListener {
                onQuantityChange(product, 1)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding = ItemCartProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.bind(products[position])
    }

    override fun getItemCount(): Int = products.size

    fun submitList(newProducts: List<CartItem>) {
        products = newProducts
        notifyDataSetChanged()
    }
}