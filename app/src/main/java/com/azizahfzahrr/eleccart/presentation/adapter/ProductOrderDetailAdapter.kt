package com.azizahfzahrr.eleccart.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.azizahfzahrr.eleccart.data.model.CategoryData
import com.azizahfzahrr.eleccart.data.model.MyOrderDetailResponse
import com.azizahfzahrr.eleccart.databinding.ActivityMyOrdersDetailBinding
import com.azizahfzahrr.eleccart.databinding.ItemPaymentProductBinding
import com.azizahfzahrr.eleccart.domain.model.OrderTransactionDetail
import com.bumptech.glide.Glide
import java.text.NumberFormat
import java.util.Locale

class ProductOrderDetailAdapter(
    private var productList: List<MyOrderDetailResponse.Data.Detail.OdProduct?>?
) : RecyclerView.Adapter<ProductOrderDetailAdapter.ProductViewHolder>() {

    fun submitList(newList: List<MyOrderDetailResponse.Data.Detail.OdProduct?>?) {
        productList = newList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ItemPaymentProductBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = productList?.get(position)
        if (product != null) {
            holder.bind(product)
        }
    }

    override fun getItemCount(): Int = productList?.size ?: 0

    inner class ProductViewHolder(
        private val binding: ItemPaymentProductBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(product: MyOrderDetailResponse.Data.Detail.OdProduct) {

            binding.tvNameProductPayment.text = product.name ?: "Unknown Product"
            binding.tvPriceProductPayment.text = formatRupiah(product.price ?: 0)
            binding.tvAmountQtyPayment.text = product.quantity.toString()

            val imageUrl = product?.imageUrl?.pdImageUrl
            if (imageUrl != null) {
                Glide.with(binding.root.context)
                    .load(imageUrl)
                    .into(binding.ivProductPayment)
            }
        }
        private fun formatRupiah(amount: Int): String {
            val localeID = Locale("in", "ID")
            val formatter = NumberFormat.getNumberInstance(localeID).apply { maximumFractionDigits = 0 }
            return "Rp${formatter.format(amount)}"
        }
    }
}
