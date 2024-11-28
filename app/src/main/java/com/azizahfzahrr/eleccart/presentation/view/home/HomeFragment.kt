package com.azizahfzahrr.eleccart.presentation.view.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.azizahfzahrr.eleccart.R
import com.azizahfzahrr.eleccart.data.model.ProductDto
import com.azizahfzahrr.eleccart.presentation.view.detailproduct.DetailProductActivity
import com.azizahfzahrr.eleccart.databinding.FragmentHomeBinding
import com.azizahfzahrr.eleccart.presentation.adapter.HomeFragmentAdapter
import com.azizahfzahrr.eleccart.data.source.local.CartManager
import com.azizahfzahrr.eleccart.presentation.view.search.SearchProductActivity
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
    private val USD_TO_IDR = 15000

    companion object {
        fun newInstance(selectedType: String?): HomeFragment {
            val fragment = HomeFragment()
            val args = Bundle()
            args.putString("selectedType", selectedType)
            fragment.arguments = args
            return fragment
        }
    }

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

        binding.searchProduct.setOnClickListener {
            val intent = Intent(requireContext(), SearchProductActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupCategoryChips() {
        val chips = listOf(
            binding.chipCategoryAll to "All",
            binding.chipCategoryTv to "TV",
            binding.chipCategoryAudio to "Audio",
            binding.chipCategorySmartphone to "Smartphone",
            binding.chipCategoryGaming to "Gaming",
            binding.chipCategoryAppliance to "Appliance"
        )

        chips.forEach { (chip, category) ->
            println("saya == $category, ${chip.isChecked}")
            if (category.uppercase() == "ALL") {
                setChipSelectedStyle(chip)
            } else {
                setChipDefaultStyle(chip)
            }

            chip.setOnClickListener {
                chips.forEach { (c, _) -> setChipDefaultStyle(c) }
                setChipSelectedStyle(chip)
                viewModel.clearProducts()
                viewModel.resetPagination()
                if (category.uppercase() == "ALL") {
                    viewModel.loadAllProducts()
                } else {
                    viewModel.loadProductsByCategory(category.uppercase())
                }
            }
        }
    }

    private fun setChipSelectedStyle(chip: Chip) {
        chip.setChipBackgroundColorResource(R.color.chip_selected_background)
        chip.setTextColor(ContextCompat.getColor(requireContext(), R.color.chip_selected_text))
        chip.isChecked = true
    }

    private fun setChipDefaultStyle(chip: Chip) {
        chip.setChipBackgroundColorResource(R.color.chip_default_background)
        chip.setTextColor(ContextCompat.getColor(requireContext(), R.color.chip_default_text))
        chip.isChecked = false
    }

    private fun setupUI() {
        val layoutManager = GridLayoutManager(context, 2)
        binding.rvListProducts.layoutManager = layoutManager

        homeFragmentAdapter = HomeFragmentAdapter(object : HomeFragmentAdapter.OnAddToCartClickListener {
            override fun onAddToCartClick(product: ProductDto.Data) {
                lifecycleScope.launch {
                    if (!cartManager.isProductInCart(product)) {
                        cartManager.addProductToCart(product)
                        Log.d("HomeFragment", "Product added to cart: ${product.pdName}")
                    } else {
                        Log.d("HomeFragment", "Product is already in the cart: ${product.pdName}")
                    }
                }
            }

            override fun onProductClick(product: ProductDto.Data) {
                val intent = Intent(requireContext(), DetailProductActivity::class.java)
                intent.putExtra("product", product)
                startActivity(intent)
            }
        })

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

            binding.shimmerLayout.stopShimmer()
            binding.shimmerLayout.visibility = View.GONE

            if (products.isNullOrEmpty()) {
                binding.rvListProducts.visibility = View.GONE
                binding.tvNoDataHome.visibility = View.VISIBLE
            } else {
                binding.rvListProducts.visibility = View.VISIBLE
                binding.tvNoDataHome.visibility = View.GONE
                homeFragmentAdapter.submitList(products)
            }
        }

        viewModel.categoryData.observe(viewLifecycleOwner) { items ->
            binding.shimmerLayout.stopShimmer()
            binding.shimmerLayout.visibility = View.GONE

            if (items.isNullOrEmpty()) {
                binding.rvListProducts.visibility = View.GONE
                binding.tvNoDataHome.visibility = View.VISIBLE
            } else {
                val productList: List<ProductDto.Data> = items.filterNotNull().map { product ->
                    ProductDto.Data(
                        pdId = product.pdId,
                        pdName = product.pdName,
                        pdImageUrl = product.pdImageUrl,
                        pdPrice = product.pdPrice,
                        pdDescription = product.pdDescription,
                        pdQuantity = product.pdQuantity,
                        totalAverageRating = product.totalAverageRating,
                        totalReviews = product.totalReviews,
                        pdData = product.pdData
                    )
                }

                binding.rvListProducts.visibility = View.VISIBLE
                binding.tvNoDataHome.visibility = View.GONE
                homeFragmentAdapter.submitList(productList)
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

        viewModel.loading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                binding.shimmerLayout.startShimmer()
                binding.shimmerLayout.visibility = View.VISIBLE
                binding.rvListProducts.visibility = View.GONE
                binding.tvNoDataHome.visibility = View.GONE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}