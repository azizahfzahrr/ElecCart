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
            }
        }
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
}

