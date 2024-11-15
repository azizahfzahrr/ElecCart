package com.azizahfzahrr.eleccart.presentation.view.payment

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.azizahfzahrr.eleccart.R
import com.azizahfzahrr.eleccart.data.local.entity.CartItem
import com.azizahfzahrr.eleccart.data.model.Item
import com.azizahfzahrr.eleccart.data.model.Order
import com.azizahfzahrr.eleccart.databinding.ActivityPaymentBinding
import com.azizahfzahrr.eleccart.presentation.view.cart.CartViewModel
import com.azizahfzahrr.eleccart.presentation.view.payment.adapter.PaymentProductAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PaymentActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPaymentBinding
    private val cartViewModel: CartViewModel by viewModels()
    private lateinit var paymentProductAdapter: PaymentProductAdapter
    private var selectedItems: List<Item> = emptyList()
    private var totalAmount = 0
    private var totalItems = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val orderRequest = intent.getSerializableExtra("order_request") as? Order

        orderRequest?.let {
            selectedItems = it.items
            totalAmount = it.amount
            totalItems = it.items.size
            updateUI()
        }
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        paymentProductAdapter = PaymentProductAdapter(selectedItems)
        binding.rvProductPayment.layoutManager = LinearLayoutManager(this)
        binding.rvProductPayment.adapter = paymentProductAdapter
    }

    private fun updateUI() {
        binding.tvTotalProductPayment.text = "$${"%.2f".format(totalAmount.toDouble())}"
        binding.tvTotalItemsPayment.text = "$totalItems items"
        paymentProductAdapter.submitList(selectedItems)
    }
}