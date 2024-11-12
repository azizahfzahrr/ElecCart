package com.azizahfzahrr.eleccart.presentation.view.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.azizahfzahrr.eleccart.DetailProductActivity
import com.azizahfzahrr.eleccart.databinding.FragmentHomeBinding
import com.azizahfzahrr.eleccart.presentation.adapter.ProductsAdapter
import com.azizahfzahrr.eleccart.data.model.ProductsResponse
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by viewModels()
    private lateinit var productsAdapter: ProductsAdapter

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
        setupUI()
        observeViewModel()
        viewModel.loadAllProducts()
        setupCategoryChips()
    }

    private fun setupCategoryChips() {
        binding.chipCategoryTv.setOnClickListener { onCategorySelected("TV", binding.chipCategoryTv) }
        binding.chipCategoryAudio.setOnClickListener { onCategorySelected("Audio", binding.chipCategoryAudio) }
        binding.chipCategorySmartphone.setOnClickListener { onCategorySelected("Smartphone", binding.chipCategorySmartphone) }
        binding.chipCategoryGaming.setOnClickListener { onCategorySelected("Gaming", binding.chipCategoryGaming) }
        binding.chipCategoryAppliance.setOnClickListener { onCategorySelected("Appliance", binding.chipCategoryAppliance) }
    }

    private fun onCategorySelected(category: String, selectedChip: Chip) {
        clearAllChipSelections()
        selectedChip.isChecked = true
        viewModel.clearProducts()
        viewModel.resetPagination()
        Log.d("Selected Category", "Category selected: $category")
        viewModel.loadProductsByCategory(category)
    }


    private fun clearAllChipSelections() {
        binding.chipCategoryTv.isChecked = false
        binding.chipCategoryAudio.isChecked = false
        binding.chipCategorySmartphone.isChecked = false
        binding.chipCategoryGaming.isChecked = false
        binding.chipCategoryAppliance.isChecked = false
    }

    private fun setupUI() {
        val layoutManager = GridLayoutManager(context, 2)
        binding.rvListProducts.layoutManager = layoutManager
        productsAdapter = ProductsAdapter { product ->
            val intent = Intent(requireContext(), DetailProductActivity::class.java)
            intent.putExtra("product", product)
            startActivity(intent)
        }
        binding.rvListProducts.adapter = productsAdapter

        binding.rvListProducts.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0) {
                    val visibleItemCount = layoutManager.childCount
                    val totalItemCount = layoutManager.itemCount
                    val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                    if (!viewModel.loading.value!! && !viewModel.isLastPage &&
                        (visibleItemCount + firstVisibleItemPosition) >= totalItemCount && firstVisibleItemPosition >= 0
                    ) {
                        viewModel.loadAllProducts()
                    }
                }
            }
        })
    }


    private fun observeViewModel() {
        viewModel.products.observe(viewLifecycleOwner) { products ->
            Log.d("HomeFragment", "Observed products: $products")
            productsAdapter.submitList(products)
            productsAdapter.notifyDataSetChanged()
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            errorMessage?.let {
                Log.e("HomeFragment", "Error: $it")
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
