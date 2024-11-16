package com.azizahfzahrr.eleccart.presentation.view.payment

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.azizahfzahrr.eleccart.data.model.Item
import com.azizahfzahrr.eleccart.data.model.Order
import com.azizahfzahrr.eleccart.databinding.ActivityPaymentBinding
import com.azizahfzahrr.eleccart.presentation.view.payment.adapter.PaymentProductAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PaymentActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPaymentBinding
    private lateinit var paymentProductAdapter: PaymentProductAdapter
    private var selectedItems: List<Item> = emptyList()
    private var totalAmount = 0
    private var totalItems = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ivLeftArrowPayment.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        paymentProductAdapter = PaymentProductAdapter()

        val orderRequest = intent.getSerializableExtra("order_request") as? Order
        if (orderRequest != null) {
            selectedItems = orderRequest.items
            totalAmount = orderRequest.amount
            totalItems = selectedItems.size
            updateUI()
        }

        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        binding.rvProductPayment.apply {
            layoutManager = LinearLayoutManager(this@PaymentActivity)
            adapter = paymentProductAdapter
        }
    }

    private fun updateUI() {
        binding.apply {
            tvTotalProductPayment.text = "Total"
            tvFillTotalItemsPayment.text = "$totalItems items"
            tvTotalPricePayment.text = "$${totalAmount}"
        }
        paymentProductAdapter.submitList(selectedItems)
    }
}