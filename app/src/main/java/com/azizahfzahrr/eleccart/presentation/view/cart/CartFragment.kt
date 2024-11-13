package com.azizahfzahrr.eleccart.presentation.view.cart

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.azizahfzahrr.eleccart.R
import com.azizahfzahrr.eleccart.data.local.entity.CartItem
import com.azizahfzahrr.eleccart.databinding.FragmentCartBinding
import com.azizahfzahrr.eleccart.presentation.adapter.CartAdapter
import com.azizahfzahrr.eleccart.data.model.ProductCart
import com.azizahfzahrr.eleccart.data.model.ProductsResponse
import com.azizahfzahrr.eleccart.data.repository.CartRepository
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CartFragment : Fragment() {

    private lateinit var cartAdapter: CartAdapter
    private var productsResponse: ProductsResponse? = null
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
            productsResponse = it.getSerializable("products_response") as? ProductsResponse
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
                if (cartItems.isEmpty()) {
                    binding.tvNoData.visibility = View.VISIBLE
                    binding.rvProductCart.visibility = View.GONE
                } else {
                    binding.tvNoData.visibility = View.GONE
                    binding.rvProductCart.visibility = View.VISIBLE
                    cartAdapter.submitList(cartItems)
                }
                updateOrderSummary()
            }
        }
        binding.ivArrowUpProductCart.setOnClickListener {
            isOrderSummaryVisible = !isOrderSummaryVisible
            updateOrderSummaryVisibility()
        }
        updateOrderSummaryVisibility()
    }

    private fun updateOrderSummaryVisibility() {
        if (isOrderSummaryVisible) {
            binding.tvOrderSummary.visibility = View.VISIBLE
            binding.tvTotalProductCart.visibility = View.VISIBLE
            binding.tvTotalPriceCart.visibility = View.VISIBLE
            binding.tvTotalItemsCart.visibility = View.VISIBLE
            binding.tvFillTotalItemsCart.visibility = View.VISIBLE
            binding.ivArrowUpProductCart.setImageResource(R.drawable.arrow_up)
        } else {
            binding.tvOrderSummary.visibility = View.VISIBLE
            binding.tvTotalProductCart.visibility = View.GONE
            binding.tvTotalPriceCart.visibility = View.GONE
            binding.tvTotalItemsCart.visibility = View.GONE
            binding.tvFillTotalItemsCart.visibility = View.GONE
            binding.ivArrowUpProductCart.setImageResource(R.drawable.arrow_down)
        }
        val visibility = if (isOrderSummaryVisible) View.VISIBLE else View.GONE
        binding.tvTotalProductCart.visibility = visibility
        binding.tvTotalPriceCart.visibility = visibility
        binding.tvTotalItemsCart.visibility = visibility
    }

    private fun removeProductFromCart(product: CartItem) {
        viewModel.deleteItemFromCart(product)
    }

    private fun updateQuantity(product: CartItem, quantityChange: Int) {
        val updatedQuantity = (product.quantity ?: 0) + quantityChange
        if (updatedQuantity > 0) {
            val updatedItem = product.copy(quantity = updatedQuantity)
            viewModel.updateItemInCart(updatedItem)
        }
    }

    private fun toggleProductSelection(product: CartItem) {
        val updatedItem = product.copy(isSelected = !product.isSelected)
        viewModel.updateItemInCart(updatedItem)
    }

    private fun updateOrderSummary() {
        val totalAmount = selectedItems.sumByDouble {
            (it.price!!.toDouble()) * (it.quantity ?: 1)
        }
        val totalItems = selectedItems.size

        binding.tvTotalPriceCart.text = "$${"%.2f".format(totalAmount)}"
        binding.tvFillTotalItemsCart.text = "$totalItems items"
    }

}