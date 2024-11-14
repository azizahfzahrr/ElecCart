package com.azizahfzahrr.eleccart.presentation.view.wishlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.azizahfzahrr.eleccart.databinding.FragmentWishlistBinding
import com.azizahfzahrr.eleccart.presentation.adapter.WishlistAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WishlistFragment : Fragment() {

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
        wishlistAdapter = WishlistAdapter(wishlistViewModel)

        binding.rvProductWishlist.layoutManager = LinearLayoutManager(requireContext())
        binding.rvProductWishlist.adapter = wishlistAdapter

        wishlistViewModel.wishlistProducts.observe(viewLifecycleOwner) { products ->
            wishlistAdapter.wishlistProducts = products
            wishlistAdapter.notifyDataSetChanged()
        }

        binding.ivLeftArrowWishlist.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}



