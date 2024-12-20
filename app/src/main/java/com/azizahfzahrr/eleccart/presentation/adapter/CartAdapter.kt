package com.azizahfzahrr.eleccart.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.azizahfzahrr.eleccart.data.local.entity.CartItem
import com.azizahfzahrr.eleccart.databinding.ItemCartProductBinding
import com.bumptech.glide.Glide
import java.text.NumberFormat
import java.util.Locale

class CartAdapter(
    private var products: List<CartItem>,
    private val onRemoveClick: (CartItem) -> Unit,
    private val onQuantityChange: (CartItem, Int) -> Unit,
    private val onCheckboxClick: (CartItem, Boolean) -> Unit
) : ListAdapter<CartItem, CartAdapter.CartViewHolder>(CartItemDiffCallback()) {

    private val selectedItems = mutableListOf<CartItem>()

    inner class CartViewHolder(private val binding: ItemCartProductBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(product: CartItem) {
            binding.tvNameProductCart.text = product.title
            val totalPrice = product.price ?: 0

            val formattedPrice = formatToRupiah(totalPrice)
            binding.tvPriceProductCart.text = formattedPrice

            binding.tvFillAmountProductCart.text = product.quantity.toString()

            Glide.with(binding.ivProductCart.context)
                .load(product.image)
                .into(binding.ivProductCart)

            binding.checkboxProductCart.setOnCheckedChangeListener(null)
            binding.checkboxProductCart.isChecked = product.isSelected
            binding.checkboxProductCart.setOnCheckedChangeListener { _, isChecked ->
                onCheckboxClick(product, isChecked)
            }
            binding.cardviewDeleteCart.setOnClickListener {
                onRemoveClick(product)
            }

            binding.cardviewMinusCart.setOnClickListener {
                if ((product.quantity ?: 1) > 1) {
                    onQuantityChange(product, -1)
                }
            }

            binding.cardviewPlusCart.setOnClickListener {
                onQuantityChange(product, 1)
            }
        }

        private fun formatToRupiah(price: Int): String {
            val rupiahFormat = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
            rupiahFormat.maximumFractionDigits = 0
            return rupiahFormat.format(price)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding =
            ItemCartProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val product = getItem(position)
        holder.bind(product)
    }

    fun getSelectedItems(): List<CartItem> = selectedItems

    private fun updateSelection(product: CartItem, isSelected: Boolean) {
        val updatedProducts = currentList.toMutableList().apply {
            val index = indexOfFirst { it.productId == product.productId }
            if (index >= 0) {
                val updatedProduct = get(index).copy(isSelected = isSelected)
                set(index, updatedProduct)
            }
        }
        submitList(updatedProducts)
    }
}