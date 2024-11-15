package com.azizahfzahrr.eleccart.presentation.view.cart

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
import com.azizahfzahrr.eleccart.data.model.ProductCart
import com.azizahfzahrr.eleccart.data.model.ProductDto
import com.azizahfzahrr.eleccart.data.model.ProductsResponse
import com.azizahfzahrr.eleccart.data.repository.CartRepository
import com.azizahfzahrr.eleccart.presentation.view.payment.PaymentActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class CartFragment : Fragment() {

    private lateinit var cartAdapter: CartAdapter
    private var productsResponse: ProductDto? = null
    private lateinit var binding: FragmentCartBinding
    private val viewModel: CartViewModel by viewModels()

    private val selectedItems = mutableListOf<CartItem>()
    private var isOrderSummaryVisible = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCartBinding.inflate(inflater, container, false)
        arguments?.let {
            productsResponse = it.getSerializable("products_response") as? ProductDto
        }
        setupRecyclerView()
        return binding.root
    }

    private fun setupRecyclerView() {
        cartAdapter = CartAdapter(
            products = emptyList(),
            onRemoveClick = { product -> removeProductFromCart(product) },
            onQuantityChange = { product, quantityChange -> updateQuantity(product, quantityChange) },
            onCheckboxClick = { product -> toggleProductSelection(product) }
        )
        binding.rvProductCart.adapter = cartAdapter
        binding.rvProductCart.layoutManager = LinearLayoutManager(requireContext())

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.allCartItems.collect { cartItems ->
                if (cartItems.isNullOrEmpty()) {
                    binding.tvNoData.visibility = View.VISIBLE
                    binding.ivDataEmpty.visibility = View.VISIBLE
                    binding.rvProductCart.visibility = View.GONE
                    binding.tvOrderSummary.visibility = View.GONE
                    binding.tvTotalProductCart.visibility = View.GONE
                    binding.tvTotalPriceCart.visibility = View.GONE
                    binding.tvTotalItemsCart.visibility = View.GONE
                    binding.tvFillTotalItemsCart.visibility = View.GONE
                    binding.ivArrowUpProductCart.visibility = View.GONE
                    binding.btnPaymentNow.visibility = View.GONE
                } else {
                    binding.tvNoData.visibility = View.GONE
                    binding.ivDataEmpty.visibility = View.GONE
                    binding.rvProductCart.visibility = View.VISIBLE

                    cartAdapter.submitList(cartItems)
                    updateOrderSummary()

                    binding.tvOrderSummary.visibility = View.VISIBLE
                    binding.tvTotalProductCart.visibility = View.VISIBLE
                    binding.tvTotalPriceCart.visibility = View.VISIBLE
                    binding.tvTotalItemsCart.visibility = View.VISIBLE
                    binding.tvFillTotalItemsCart.visibility = View.VISIBLE
                    binding.ivArrowUpProductCart.visibility = View.VISIBLE
                    binding.btnPaymentNow.visibility = View.VISIBLE
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

    private fun toggleProductSelection(product: CartItem) {
        if (product.isSelected) {
            selectedItems.add(product)
        } else {
            selectedItems.removeAll { it.productId == product.productId }
        }
        updateOrderSummary()

        // Update product selection state in the database
        val updatedItem = product.copy(isSelected = !product.isSelected)
        viewModel.updateItemInCart(updatedItem)
    }

    private fun updateOrderSummary() {
        val totalAmount = selectedItems.sumOf {
            it.price?.times(it.quantity ?: 1) ?: 0
        }
        val totalItems = selectedItems.sumOf { it.quantity ?: 1 }

        binding.tvTotalPriceCart.text = "$${"%.2f".format(totalAmount.toDouble())}"
        binding.tvFillTotalItemsCart.text = "$totalItems items"
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