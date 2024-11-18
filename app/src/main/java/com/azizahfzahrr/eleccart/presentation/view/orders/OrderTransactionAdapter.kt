package com.azizahfzahrr.eleccart.presentation.view.orders

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.azizahfzahrr.eleccart.databinding.ItemListTransactionBinding
import com.azizahfzahrr.eleccart.domain.model.OrderTransaction
import com.azizahfzahrr.eleccart.presentation.listener.ItemOrdersListener

class OrderTransactionAdapter(
    private var orderTransactions: List<OrderTransaction>,
    private val itemOrdersListener: ItemOrdersListener
) : RecyclerView.Adapter<OrderTransactionAdapter.OrderTransactionViewHolder>() {

    fun submitList(newItems: List<OrderTransaction>) {
        orderTransactions = newItems
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderTransactionViewHolder {
        val binding = ItemListTransactionBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return OrderTransactionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OrderTransactionViewHolder, position: Int) {
        val orderTransaction = orderTransactions[position]
        holder.bind(orderTransaction)
    }

    override fun getItemCount(): Int = orderTransactions.size

    inner class OrderTransactionViewHolder(
        private val binding: ItemListTransactionBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(orderTransaction: OrderTransaction) {
            binding.tvIdOrderTransaction.text = orderTransaction.id
            binding.tvStatusTransaction.text = orderTransaction.status
            binding.tvNumberTotalPriceTransaction.text = "$${orderTransaction.totalPrice}"
            itemView.setOnClickListener {
                itemOrdersListener.onClick(orderTransaction.id)
            }
        }
    }
}