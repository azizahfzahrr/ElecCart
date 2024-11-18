package com.azizahfzahrr.eleccart.presentation.view.cart

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.azizahfzahrr.eleccart.R
import com.azizahfzahrr.eleccart.data.local.entity.CartItem
import com.azizahfzahrr.eleccart.data.model.Item
import com.azizahfzahrr.eleccart.data.model.Order
import com.azizahfzahrr.eleccart.databinding.FragmentCartBinding
import com.azizahfzahrr.eleccart.presentation.adapter.CartAdapter
import com.azizahfzahrr.eleccart.presentation.view.payment.PaymentActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CartFragment : Fragment() {
    private lateinit var cartAdapter: CartAdapter
    private lateinit var binding: FragmentCartBinding
    private val viewModel: CartViewModel by viewModels()
    private val selectedItems = mutableListOf<CartItem>()
    private var cartItems: List<CartItem> = emptyList()
    private var isOrderSummaryVisible = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCartBinding.inflate(inflater, container, false)
        setupRecyclerView()
        return binding.root
    }

    private fun setupRecyclerView() {
        cartAdapter = CartAdapter(
            products = emptyList(),
            onRemoveClick = { product -> removeProductFromCart(product) },
            onQuantityChange = { product, quantityChange -> updateQuantity(product, quantityChange) },
            onCheckboxClick = { product, isChecked -> toggleProductSelection(product, isChecked) }
        )
        binding.rvProductCart.adapter = cartAdapter
        binding.rvProductCart.layoutManager = LinearLayoutManager(requireContext())

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.allCartItems.collect { items ->
                val updatedItems = items.map { it.copy(isSelected = false) }
                cartItems = updatedItems
            //    cartItems = items
                if (cartItems.isNullOrEmpty()) {
                    showEmptyCartUI()
                } else {
                    showCartWithDataUI(cartItems)
                }
            }
        }

        binding.ivArrowUpProductCart.setOnClickListener {
            isOrderSummaryVisible = !isOrderSummaryVisible
            updateOrderSummaryVisibility()
        }

        binding.btnPaymentNow.setOnClickListener {
            navigateToPayment()
        }

        updateOrderSummaryVisibility()
        updatePaymentButtonState()
    }

    private fun showEmptyCartUI() {
        binding.apply {
            tvNoData.visibility = View.VISIBLE
            ivDataEmpty.visibility = View.VISIBLE
            rvProductCart.visibility = View.GONE
            tvOrderSummary.visibility = View.GONE
            tvTotalProductCart.visibility = View.GONE
            tvTotalPriceCart.visibility = View.GONE
            tvTotalItemsCart.visibility = View.GONE
            tvFillTotalItemsCart.visibility = View.GONE
            ivArrowUpProductCart.visibility = View.GONE
            btnPaymentNow.visibility = View.GONE
        }
    }

    private fun showCartWithDataUI(cartItems: List<CartItem>) {
        binding.apply {
            tvNoData.visibility = View.GONE
            ivDataEmpty.visibility = View.GONE
            rvProductCart.visibility = View.VISIBLE
            tvOrderSummary.visibility = View.VISIBLE
            tvTotalProductCart.visibility = View.VISIBLE
            tvTotalPriceCart.visibility = View.VISIBLE
            tvTotalItemsCart.visibility = View.VISIBLE
            tvFillTotalItemsCart.visibility = View.VISIBLE
            ivArrowUpProductCart.visibility = View.VISIBLE
            btnPaymentNow.visibility = View.VISIBLE
        }
        if (cartItems.isEmpty()) {
            showEmptyCartUI()
            return
        }
        if (binding.rvProductCart.isComputingLayout) {
            binding.rvProductCart.post {
                cartAdapter.submitList(cartItems)
                updateOrderSummary()
            }
        } else {
            cartAdapter.submitList(cartItems)
            updateOrderSummary()
        }
    }

    private fun navigateToPayment() {
        if (selectedItems.isEmpty()) {
            Toast.makeText(context, "Please select at least one product to proceed", Toast.LENGTH_SHORT).show()
            return
        }

        val orderItems = selectedItems.map { cartItem ->
            Item(
                id = cartItem.productId.toIntOrNull() ?: 0,
                name = cartItem.title ?: "",
                price = cartItem.price ?: 0,
                quantity = cartItem.quantity ?: 1
            )
        }

        val totalAmount = selectedItems.sumOf { (it.price ?: 0) * (it.quantity ?: 1) }
        val orderRequest = Order(
            amount = totalAmount,
            email = "user@example.com",
            items = orderItems
        )

        val intent = Intent(context, PaymentActivity::class.java)
        intent.putExtra("order_request", orderRequest)
        startActivity(intent)
    }

    private fun removeProductFromCart(product: CartItem) {
        viewModel.deleteItemFromCart(product)
        selectedItems.remove(product)
        updateOrderSummary()
    }

    private fun updateQuantity(product: CartItem, quantityChange: Int) {
        val updatedQuantity = (product.quantity ?: 0) + quantityChange
        if (updatedQuantity > 0) {
            val updatedItem = product.copy(quantity = updatedQuantity)
            viewModel.updateItemInCart(updatedItem)
            if (product.isSelected) {
                selectedItems.find { it.productId == product.productId }?.quantity = updatedQuantity
                updateOrderSummary()
            }
        }
    }

    private fun toggleProductSelection(product: CartItem, isChecked: Boolean) {
        val updatedItem = product.copy(isSelected = isChecked)

        viewModel.updateItemInCart(updatedItem)

        if (isChecked) {
            if (!selectedItems.any { it.productId == updatedItem.productId }) {
                selectedItems.add(updatedItem)
            }
        } else {
            selectedItems.removeIf { it.productId == updatedItem.productId }
        }
        updateOrderSummary()
        updatePaymentButtonState()
    }

    private fun updateOrderSummary() {
        val hasSelectedItems = selectedItems.isNotEmpty()
        val totalAmount = selectedItems.sumOf { it.price?.times(it.quantity ?: 1) ?: 0 }
        val totalItems = selectedItems.sumOf { it.quantity ?: 1 }

        binding.tvTotalPriceCart.text = "$${totalAmount}"
        binding.tvFillTotalItemsCart.text = "$totalItems items"

        if (!hasSelectedItems){
            binding.tvTotalPriceCart.visibility = View.GONE
            binding.tvFillTotalItemsCart.visibility = View.GONE
        } else {
            binding.tvTotalPriceCart.visibility = View.VISIBLE
            binding.tvFillTotalItemsCart.visibility = View.VISIBLE
        }
        updatePaymentButtonState()
    }

    private fun updatePaymentButtonState() {
        binding.btnPaymentNow.isEnabled = selectedItems.isNotEmpty()
        binding.btnPaymentNow.setBackgroundColor(
            ContextCompat.getColor(
                requireContext(),
                if (selectedItems.isEmpty()) R.color.grey_300 else R.color.color_primary
            )
        )
    }

    private fun updateOrderSummaryVisibility() {
        if (isOrderSummaryVisible) {
            binding.tvTotalProductCart.visibility = View.VISIBLE
            binding.tvTotalPriceCart.visibility = View.VISIBLE
            binding.tvTotalItemsCart.visibility = View.VISIBLE
            binding.tvFillTotalItemsCart.visibility = View.VISIBLE
            binding.ivArrowUpProductCart.setImageResource(R.drawable.arrow_up)
        } else {
            binding.tvTotalProductCart.visibility = View.GONE
            binding.tvTotalPriceCart.visibility = View.GONE
            binding.tvTotalItemsCart.visibility = View.GONE
            binding.tvFillTotalItemsCart.visibility = View.GONE
            binding.ivArrowUpProductCart.setImageResource(R.drawable.arrow_down)
        }
    }
}