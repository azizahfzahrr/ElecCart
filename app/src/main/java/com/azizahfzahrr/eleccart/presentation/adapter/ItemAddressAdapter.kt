package com.azizahfzahrr.eleccart.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.azizahfzahrr.eleccart.data.source.local.AddressEntity
import com.azizahfzahrr.eleccart.databinding.ItemListAddressBinding
import com.azizahfzahrr.eleccart.presentation.listener.ItemAddressListener

class ItemAddressAdapter(
    private var listAddress: List<AddressEntity>,
    private val listener: ItemAddressListener,
    private val selectedAddressListener: SelectedAddressListener
) : RecyclerView.Adapter<ItemAddressAdapter.MyViewHolder>() {

    private var selectedAddressPosition = -1

    class MyViewHolder(val binding: ItemListAddressBinding) : RecyclerView.ViewHolder(binding.root)

    interface SelectedAddressListener {
        fun onAddressSelected(address: AddressEntity?)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemListAddressBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val address = listAddress[position]

        holder.binding.tvNameRecipient.text = address.addressRecipientName
        holder.binding.tvPhoneNumberRecipient.text = address.addressRecipientPhone
        holder.binding.tvAddressRecipient.text = address.addressRecipientFullAddress
        holder.binding.tvProvince.text = address.addressRecipientProvince
        holder.binding.tvPostalCode.text = address.addressRecipientPostalCode

        holder.binding.rbSelectItem.isChecked = selectedAddressPosition == position

        holder.binding.rbSelectItem.setOnClickListener {
            val previousPosition = selectedAddressPosition
            selectedAddressPosition = if (selectedAddressPosition == position) {
                -1
            } else {
                position
            }

            notifyItemChanged(previousPosition)
            notifyItemChanged(selectedAddressPosition)

            if (selectedAddressPosition != -1) {
                selectedAddressListener.onAddressSelected(listAddress[selectedAddressPosition])
            } else {
                selectedAddressListener.onAddressSelected(null)
            }
        }

        holder.binding.ivEditAddress.setOnClickListener {
            listener.onEdit(address)
        }

        holder.binding.ivDeleteAddress.setOnClickListener {
            listener.onDelete(address)
        }
    }

    override fun getItemCount() = listAddress.size

    fun updateDataAddress(newAddress: List<AddressEntity>) {
        listAddress = newAddress
        notifyDataSetChanged()
    }

    fun getSelectedAddress(): AddressEntity? {
        return listAddress.getOrNull(selectedAddressPosition)
    }
}
