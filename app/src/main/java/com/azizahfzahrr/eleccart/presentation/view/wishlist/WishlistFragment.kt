package com.azizahfzahrr.eleccart.presentation.view.wishlist

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.azizahfzahrr.eleccart.R
import com.azizahfzahrr.eleccart.data.source.local.WishlistEntity
import com.azizahfzahrr.eleccart.databinding.FragmentWishlistBinding
import com.azizahfzahrr.eleccart.presentation.adapter.WishlistAdapter
import com.azizahfzahrr.eleccart.presentation.listener.ItemWishlistListener
import com.azizahfzahrr.eleccart.presentation.view.cart.CartFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WishlistFragment : Fragment(), ItemWishlistListener {

    private lateinit var wishlistViewModel: WishlistViewModel
    private lateinit var wishlistAdapter: WishlistAdapter
    private var _binding: FragmentWishlistBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWishlistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        wishlistViewModel = ViewModelProvider(this).get(WishlistViewModel::class.java)
        wishlistAdapter = WishlistAdapter(wishlistViewModel, this)

        binding.rvProductWishlist.layoutManager = LinearLayoutManager(requireContext())
        binding.rvProductWishlist.adapter = wishlistAdapter

        wishlistViewModel.wishlistProducts.observe(viewLifecycleOwner, Observer { products ->
            if (products.isNullOrEmpty()) {
                showEmptyWishlistUI()
            } else {
                showWishlistUI(products)
            }
        })

        wishlistViewModel.loadWishlistProducts()
    }

    private fun showWishlistUI(products: List<WishlistEntity>) {
        binding.apply {
            rvProductWishlist.visibility = View.VISIBLE
            tvNoDataWishlist.visibility = View.GONE
            tvNoDataDetailWishlist.visibility = View.GONE
            ivDataEmptyWishlist.visibility = View.GONE
        }
        wishlistAdapter.wishlistProducts = products
    }

    private fun showEmptyWishlistUI() {
        binding.apply {
            tvNoDataWishlist.visibility = View.VISIBLE
            tvNoDataDetailWishlist.visibility = View.VISIBLE
            ivDataEmptyWishlist.visibility = View.VISIBLE
            rvProductWishlist.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onAddToCartClicked(product: WishlistEntity) {
        wishlistViewModel.addProductToCart(product)

        val cartFragment = CartFragment()
        val args = Bundle().apply {
            putString("productId", product.productId)
            putString("title", product.title)
            putString("price", product.price.toString())
            putString("image", product.image)
        }
        cartFragment.arguments = args

        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, cartFragment)
            .addToBackStack(null)
            .commit()
    }


    override fun onDeleteFromWishlistClicked(productId: String) {
        wishlistViewModel.removeProductFromWishlist(productId)
    }
}