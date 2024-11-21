package com.azizahfzahrr.eleccart.presentation.view.orders

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.azizahfzahrr.eleccart.R
import com.azizahfzahrr.eleccart.data.model.MyOrderDetailResponse
import com.azizahfzahrr.eleccart.databinding.ActivityMyOrdersDetailBinding
import com.azizahfzahrr.eleccart.domain.model.MyOrderDetailState
import com.azizahfzahrr.eleccart.domain.model.OrderTransactionDetail
import com.azizahfzahrr.eleccart.presentation.adapter.ProductOrderDetailAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MyOrdersDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMyOrdersDetailBinding
    private lateinit var adapter: ProductOrderDetailAdapter
    private val viewModel: MyOrdersDetailViewModel by viewModels()
    private var orderTransactionId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyOrdersDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()

        binding.ivLeftArrowDetailOrders.setOnClickListener {
            onBackPressed()
        }

        orderTransactionId = intent.getStringExtra("id_transaction_order")
        Log.d("OrderTransactionID", "Transaction ID: $orderTransactionId")

        if (orderTransactionId != null) {
            loadOrderDetails(orderTransactionId!!)
        }
        observeViewModel()
    }

    private fun setupRecyclerView() {
        adapter = ProductOrderDetailAdapter(emptyList())
        binding.rvProductOrderDetail.apply {
            layoutManager = LinearLayoutManager(this@MyOrdersDetailActivity)
            adapter = this@MyOrdersDetailActivity.adapter
        }
    }

    private fun loadOrderDetails(orderId: String) {
        viewModel.loadOrderDetails(orderId)
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.orderDetailState.collect { state ->
                when (state) {
                    is MyOrderDetailState.Loading -> showLoading()
                    is MyOrderDetailState.Success -> state.orderDetails?.let { showData(it) }
                    is MyOrderDetailState.Error -> showError(state.message)
                }
            }
        }
    }

    private fun showLoading() {
        binding.shimmerLayout.startShimmer()
        binding.shimmerLayout.isVisible = true
    }

    private fun showData(orderDetails: MyOrderDetailResponse.Data) {
        binding.shimmerLayout.stopShimmer()
        binding.shimmerLayout.isVisible = false

        binding.tvOrderNameDetail.text = orderDetails?.orPlatformId.toString()
        binding.tvPaymentStatus.text = orderDetails?.orStatus
        binding.tvPaymentStatusDetail.text = orderDetails?.orPaymentStatus
        binding.tvTotalPriceOrderNumberDetail.text = "$${orderDetails?.orTotalPrice}"

        val details = orderDetails?.details?.filterNotNull().orEmpty()
        adapter.submitList(details[0].odProducts)

    }

    private fun showError(message: String) {
        binding.shimmerLayout.stopShimmer()
        binding.shimmerLayout.isVisible = false
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}