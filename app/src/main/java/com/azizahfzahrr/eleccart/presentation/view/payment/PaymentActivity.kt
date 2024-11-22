package com.azizahfzahrr.eleccart.presentation.view.payment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.azizahfzahrr.eleccart.R
import com.azizahfzahrr.eleccart.data.local.entity.CartItem
import com.azizahfzahrr.eleccart.data.model.Item
import com.azizahfzahrr.eleccart.data.model.Order
import com.azizahfzahrr.eleccart.data.source.local.AddressEntity
import com.azizahfzahrr.eleccart.databinding.ActivityPaymentBinding
import com.azizahfzahrr.eleccart.domain.model.OrderState
import com.azizahfzahrr.eleccart.presentation.adapter.PaymentProductAdapter
import com.azizahfzahrr.eleccart.presentation.view.address.ChooseAddressActivity
import com.azizahfzahrr.eleccart.presentation.view.cart.CartViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

@AndroidEntryPoint
class PaymentActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPaymentBinding
    private lateinit var paymentProductAdapter: PaymentProductAdapter
    private val viewModel: CartViewModel by viewModels()
    private val orderViewModel: OrderViewModel by viewModels()
    private var selectedItems: List<Item> = emptyList()
    private var totalAmount = 0
    private var totalItems = 0
    private var isOrderSummaryVisible = true

    companion object {
        private const val REQUEST_CHOOSE_ADDRESS = 1001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ivArrowRightPayment.setOnClickListener {
            val intent = Intent(this@PaymentActivity, ChooseAddressActivity::class.java)
            startActivityForResult(intent, REQUEST_CHOOSE_ADDRESS)
        }

        binding.ivLeftArrowPayment.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        paymentProductAdapter = PaymentProductAdapter()

        val orderRequest = intent.getSerializableExtra("order_request") as? Order
        if (orderRequest != null) {
            selectedItems = orderRequest.items
            totalAmount = calculateTotalAmount(selectedItems)
            totalItems = calculateTotalItems(selectedItems)
            updateUI()
        } else {
            Log.e("PaymentActivity", "Order request is null!")
        }
        setupRecyclerView()

        binding.btnContinuePayment.setOnClickListener {
            orderViewModel.createOrder(
                Order(
                    amount = totalAmount,
                    email = "test@gmail.com",
                    items = selectedItems
                )
            )
        }

        lifecycleScope.launch {
            orderViewModel.orderState.collect { orderState ->
                when (orderState) {
                    is OrderState.Loading -> {
                        Toast.makeText(this@PaymentActivity, "Loading", Toast.LENGTH_SHORT).show()
                    }
                    is OrderState.Success -> {
                        Toast.makeText(
                            this@PaymentActivity,
                            "Order Created Successfully",
                            Toast.LENGTH_SHORT
                        ).show()

                        val cartItemsToDelete = selectedItems.map { item ->
                            CartItem(
                                productId = item.id.toString(),
                                title = item.name,
                                price = item.price,
                                image = "",
                                quantity = item.quantity,
                                isSelected = false
                            )
                        }

                        viewModel.deleteItemsFromCart(cartItemsToDelete)
                        viewModel.updatePaymentStatus(true)
                    }
                    is OrderState.SuccessPayment -> {
                        val intent = Intent(this@PaymentActivity, PaymentWebViewActivity::class.java).apply {
                            putExtra("url_payment", orderState.paymentUrl)
                            putExtra("product", ArrayList(selectedItems))
                        }
                        startActivity(intent)
                    }
                    is OrderState.Error -> {
                        Toast.makeText(
                            this@PaymentActivity,
                            "Error: ${orderState.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CHOOSE_ADDRESS && resultCode == RESULT_OK) {
            val selectedAddress = data?.getParcelableExtra<AddressEntity>("selected_address")
            selectedAddress?.let { updateAddressDetails(it) }
        }
    }

    private fun setupRecyclerView() {
        binding.rvProductPayment.apply {
            layoutManager = LinearLayoutManager(this@PaymentActivity)
            adapter = paymentProductAdapter

            binding.ivArrowUpProductPayment.setOnClickListener {
                isOrderSummaryVisible = !isOrderSummaryVisible
                updatePaymentSummaryVisibility()
            }
        }
    }

    private fun updatePaymentSummaryVisibility() {
        if (isOrderSummaryVisible) {
            binding.tvTotalProductPayment.visibility = View.VISIBLE
            binding.tvTotalPricePayment.visibility = View.VISIBLE
            binding.tvTotalItemsPayment.visibility = View.VISIBLE
            binding.tvFillTotalItemsPayment.visibility = View.VISIBLE
            binding.ivArrowUpProductPayment.setImageResource(R.drawable.arrow_up)
        } else {
            binding.tvTotalProductPayment.visibility = View.GONE
            binding.tvTotalPricePayment.visibility = View.GONE
            binding.tvTotalItemsPayment.visibility = View.GONE
            binding.tvFillTotalItemsPayment.visibility = View.GONE
            binding.ivArrowUpProductPayment.setImageResource(R.drawable.arrow_down)
        }
    }

    private fun updateUI() {
        binding.apply {
            tvFillTotalItemsPayment.text = "$totalItems items"
            val formattedTotalPrice = "Rp${formatRupiah(totalAmount)}"
            tvTotalPricePayment.text = formattedTotalPrice
        }
        paymentProductAdapter.submitList(selectedItems)
        updatePaymentSummaryVisibility()
    }


    private fun formatRupiah(amount: Int): String {
        val localeID = Locale("in", "ID")
        val formatter = NumberFormat.getNumberInstance(localeID).apply { maximumFractionDigits = 0 }
        return formatter.format(amount)
    }


    private fun updateAddressDetails(address: AddressEntity) {
        binding.tvFullNamePayment.text = address.addressRecipientName
        binding.tvFullPhoneNumber.text = address.addressRecipientPhone
        binding.tvFullAddressPayment.text = address.addressRecipientFullAddress
        binding.tvProvincePayment.text = address.addressRecipientProvince
        binding.tvPostalCode.text = address.addressRecipientPostalCode
    }

    private fun calculateTotalAmount(items: List<Item>): Int {
        return items.sumOf { it.price * it.quantity }
    }

    private fun calculateTotalItems(items: List<Item>): Int {
        return items.sumOf { it.quantity }
    }
}