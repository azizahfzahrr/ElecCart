package com.azizahfzahrr.eleccart.presentation.view.payment

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.azizahfzahrr.eleccart.R
import com.azizahfzahrr.eleccart.data.model.Item
import com.azizahfzahrr.eleccart.data.model.Order
import com.azizahfzahrr.eleccart.data.source.local.AddressEntity
import com.azizahfzahrr.eleccart.databinding.ActivityPaymentBinding
import com.azizahfzahrr.eleccart.presentation.adapter.PaymentProductAdapter
import com.azizahfzahrr.eleccart.presentation.view.address.ChooseAddressActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PaymentActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPaymentBinding
    private lateinit var paymentProductAdapter: PaymentProductAdapter
    private var selectedItems: List<Item> = emptyList()
    private var totalAmount = 0
    private var totalItems = 0

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
            totalAmount = orderRequest.amount
            totalItems = selectedItems.size
            updateUI()
        }

        setupRecyclerView()

        binding.btnContinuePayment.setOnClickListener {
            val intent = Intent(this@PaymentActivity, PaymentWebViewActivity::class.java)
            startActivity(intent)
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

    private fun updateAddressDetails(address: AddressEntity) {
        binding.tvFullNamePayment.text = address.addressRecipientName
        binding.tvFullPhoneNumber.text = address.addressRecipientPhone
        binding.tvFullAddressPayment.text = address.addressRecipientFullAddress
        binding.tvProvincePayment.text = address.addressRecipientProvince
        binding.tvPostalCode.text = address.addressRecipientPostalCode
    }
}