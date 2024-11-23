package com.azizahfzahrr.eleccart.presentation.view.orders

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
import androidx.recyclerview.widget.LinearLayoutManager
import com.azizahfzahrr.eleccart.R
import com.azizahfzahrr.eleccart.databinding.ActivityMyOrdersBinding
import com.azizahfzahrr.eleccart.domain.model.MyOrderResponse
import com.azizahfzahrr.eleccart.domain.model.MyOrderState
import com.azizahfzahrr.eleccart.domain.model.OrderTransaction
import com.azizahfzahrr.eleccart.domain.model.OrderTransactionDetail
import com.azizahfzahrr.eleccart.domain.model.OrderTransactionState
import com.azizahfzahrr.eleccart.presentation.listener.ItemOrdersListener
import com.azizahfzahrr.eleccart.presentation.view.profile.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MyOrdersActivity : AppCompatActivity(), ItemOrdersListener {

    private lateinit var binding: ActivityMyOrdersBinding
    private val viewModel: MyOrdersViewModel by viewModels()
    private val userViewModel: ProfileViewModel by viewModels()
    private lateinit var adapter: OrderTransactionAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyOrdersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupRecyclerView()
        observeViewModel()
        getCurrentUserEmail { email ->
            viewModel.loadAllOrderTransaction(email)
        }
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbarMyOrders)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = getString(R.string.my_orders)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setupRecyclerView() {
        adapter = OrderTransactionAdapter(emptyList(), this)
        binding.rvMyTransaction.layoutManager = LinearLayoutManager(this)
        binding.rvMyTransaction.adapter = adapter
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.orderTransactionState.collect { state ->
                when (state) {
                    is MyOrderState.Loading -> showLoading()
                    is MyOrderState.Success -> showData(state.transactionOrder)
                    is MyOrderState.SuccessDetail -> {
                        // Handle or ignore the SuccessDetail case
                        // This activity doesn't need to do anything with detailed data
                    }
                    is MyOrderState.Error -> showError(state.message)
                }
            }
        }
    }


    private fun showLoading() {
        binding.shimmerLayout.startShimmer()
        binding.shimmerLayout.isVisible = true
        binding.rvMyTransaction.isVisible = false
    }

    private fun showData(orders: List<MyOrderResponse.Data?>) {
        binding.shimmerLayout.stopShimmer()
        binding.shimmerLayout.isVisible = false
        binding.rvMyTransaction.isVisible = true
        adapter.submitList(orders)
    }

    private fun showError(message: String) {
        binding.shimmerLayout.stopShimmer()
        binding.shimmerLayout.isVisible = false
        binding.rvMyTransaction.isVisible = false
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

        Log.d("Message", message)
    }

    private fun getCurrentUserEmail(callback: (String) -> Unit) {
        userViewModel.user.observe(this) { user ->
            callback(user.email)
        }
    }


    override fun onClick(id: String) {
        Log.d("OrderTransactionID", id)
        val intent = Intent(this, MyOrdersDetailActivity::class.java)
        intent.putExtra("id_transaction_order", id)
        startActivity(intent)
    }
}