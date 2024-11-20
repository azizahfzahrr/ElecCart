package com.azizahfzahrr.eleccart.presentation.view.search

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.azizahfzahrr.eleccart.R
import com.azizahfzahrr.eleccart.databinding.ActivitySearchProductBinding
import com.azizahfzahrr.eleccart.domain.model.ProductState
import com.azizahfzahrr.eleccart.domain.model.Products
import com.azizahfzahrr.eleccart.presentation.adapter.ItemProductAdapter
import com.azizahfzahrr.eleccart.presentation.listener.ItemProductListener
import com.azizahfzahrr.eleccart.presentation.view.detailproduct.DetailProductActivity
import com.azizahfzahrr.eleccart.presentation.view.detailproduct.DetailProductViewModel
import com.google.android.material.search.SearchView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchProductActivity : AppCompatActivity(), ItemProductListener {

    private lateinit var binding: ActivitySearchProductBinding
    private val viewModel: SearchProductViewModel by viewModels()

    private val adapter by lazy {
        ItemProductAdapter { id ->
            onClick(id)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarSearchProduct)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.left_arrow)

        binding.rvProductsSearch.layoutManager = GridLayoutManager(this, 2)
        binding.rvProductsSearch.adapter = adapter

        viewModel.loadAllProducts(search = null, limit = null)

        binding.svSearchProduct.setOnQueryTextListener(object :
            android.widget.SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(search: String?): Boolean {
                if (!search.isNullOrEmpty()) {
                    viewModel.loadAllProducts(search, null)
                }
                binding.svSearchProduct.clearFocus()
                return true
            }

            override fun onQueryTextChange(newSearch: String?): Boolean {
                if (newSearch.isNullOrEmpty()) {
                    viewModel.loadAllProducts(null, null)
                }
                return true
            }
        })

        lifecycleScope.launch {
            viewModel.productState.collect { productState ->
                when (productState) {
                    is ProductState.Loading -> {
                        binding.shimmerLayout.startShimmer()
                        binding.shimmerLayout.isVisible = true
                        binding.rvProductsSearch.isVisible = false
                    }
                    is ProductState.Error -> {
                        binding.shimmerLayout.stopShimmer()
                        binding.shimmerLayout.isVisible = false
                        binding.rvProductsSearch.isVisible = false
                        Toast.makeText(this@SearchProductActivity, productState.message, Toast.LENGTH_SHORT).show()
                    }
                    is ProductState.Success -> {
                        binding.shimmerLayout.stopShimmer()
                        binding.shimmerLayout.isVisible = false
                        binding.rvProductsSearch.isVisible = true
                        adapter.submitList(productState.products)
                    }
                    else -> Unit
                }
            }
        }

    }

    override fun onClick(id: Int) {
        val product = viewModel.getProductById(id)
        if (product != null) {
            val intent = Intent(this, DetailProductActivity::class.java).apply {
                putExtra("product", product)
            }
            startActivity(intent)
        } else {
            Toast.makeText(this, "Invalid product data", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
