package com.azizahfzahrr.eleccart.presentation.view.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.azizahfzahrr.eleccart.DetailProductActivity
import com.azizahfzahrr.eleccart.databinding.FragmentHomeBinding
import com.azizahfzahrr.eleccart.presentation.adapter.HomeFragmentAdapter
import com.azizahfzahrr.eleccart.data.model.ProductsResponse
import com.azizahfzahrr.eleccart.data.source.local.CartManager
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by viewModels()
    private lateinit var homeFragmentAdapter: HomeFragmentAdapter
    private lateinit var cartManager: CartManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cartManager = CartManager(requireContext())
        setupUI()
        observeViewModel()
        viewModel.loadAllProducts()
        setupCategoryChips()
    }

    private fun setupCategoryChips() {
        val chips = listOf(
            binding.chipCategoryTv to "TV",
            binding.chipCategoryAudio to "Audio",
            binding.chipCategorySmartphone to "Smartphone",
            binding.chipCategoryGaming to "Gaming",
            binding.chipCategoryAppliance to "Appliance"
        )

        chips.forEach { (chip, category) ->
            chip.setOnClickListener { onCategorySelected(category, chip) }
        }
    }

    private fun onCategorySelected(category: String, selectedChip: Chip) {
        clearAllChipSelections()
        selectedChip.isChecked = true
        viewModel.clearProducts()
        viewModel.resetPagination()
        Log.d("HomeFragment", "Category selected: $category")
        viewModel.loadProductsByCategory(category)
    }

    private fun clearAllChipSelections() {
        listOf(
            binding.chipCategoryTv,
            binding.chipCategoryAudio,
            binding.chipCategorySmartphone,
            binding.chipCategoryGaming,
            binding.chipCategoryAppliance
        ).forEach { it.isChecked = false }
    }

    private fun setupUI() {
        val layoutManager = GridLayoutManager(context, 2)
        binding.rvListProducts.layoutManager = layoutManager

        homeFragmentAdapter = HomeFragmentAdapter(object : HomeFragmentAdapter.OnAddToCartClickListener {
            override fun onAddToCartClick(product: ProductsResponse.Product) {
                lifecycleScope.launch {
                    if (!cartManager.isProductInCart(product)) {
                        cartManager.addProductToCart(product)
                        Log.d("HomeFragment", "Product added to cart: ${product.title}")
                    } else {
                        Log.d("HomeFragment", "Product is already in the cart: ${product.title}")
                    }
                }
            }
            override fun onProductClick(product: ProductsResponse.Product) {
                val intent = Intent(requireContext(), DetailProductActivity::class.java)
                intent.putExtra("product", product)
                startActivity(intent)
            }
        })

        binding.rvListProducts.adapter = homeFragmentAdapter
        binding.rvListProducts.adapter = homeFragmentAdapter

        binding.rvListProducts.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0 && !viewModel.loading.value!! && !viewModel.isLastPage) {
                    val visibleItemCount = layoutManager.childCount
                    val totalItemCount = layoutManager.itemCount
                    val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount) {
                        viewModel.loadAllProducts()
                    }
                }
            }
        })
    }

    private fun observeViewModel() {
        viewModel.products.observe(viewLifecycleOwner) { products ->
            Log.d("HomeFragment", "Observed products: ${products.size} items")

            if (products.isNullOrEmpty()) {
                binding.rvListProducts.visibility = View.GONE
                binding.tvNoDataHome.visibility = View.VISIBLE
            } else {
                binding.rvListProducts.visibility = View.VISIBLE
                binding.tvNoDataHome.visibility = View.GONE
                homeFragmentAdapter.submitList(products)
            }
        }
        viewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            errorMessage?.let {
                Log.e("HomeFragment", "Error: $it")
                binding.tvNoDataHome.text = "Failed to load products"
                binding.tvNoDataHome.visibility = View.VISIBLE
                binding.rvListProducts.visibility = View.GONE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}