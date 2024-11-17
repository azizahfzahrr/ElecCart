package com.azizahfzahrr.eleccart.presentation.view.address

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.azizahfzahrr.eleccart.R
import com.azizahfzahrr.eleccart.data.model.Address
import com.azizahfzahrr.eleccart.data.source.local.AddressEntity
import com.azizahfzahrr.eleccart.databinding.ActivityChooseAddressBinding
import com.azizahfzahrr.eleccart.databinding.ActivityCreateAddressBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateAddressActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateAddressBinding
    private val viewModel: CreateAddressViewModel by viewModels()
    private var editAddress: AddressEntity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateAddressBinding.inflate(layoutInflater)
        setContentView(binding.root)

        editAddress = intent.getParcelableExtra("editAddress")
        editAddress?.let {
            binding.etFullName.setText(it.addressRecipientName)
            binding.etPhoneNumber.setText(it.addressRecipientPhone)
            binding.etFullAddress.setText(it.addressRecipientFullAddress)
            binding.etProvince.setText(it.addressRecipientProvince)
            binding.etPostalCode.setText(it.addressRecipientPostalCode)
        }

        binding.btnSaveCreateAddress.setOnClickListener {
            val fullName = binding.etFullName.text.toString().trim()
            val phoneNumber = binding.etPhoneNumber.text.toString().trim()
            val fullAddress = binding.etFullAddress.text.toString().trim()
            val province = binding.etProvince.text.toString().trim()
            val postalCode = binding.etPostalCode.text.toString().trim()

            if (fullName.isEmpty() || phoneNumber.isEmpty() || fullAddress.isEmpty() || province.isEmpty() || postalCode.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            } else {
                val address = AddressEntity(
                    id = editAddress?.id ?: 0,
                    addressRecipientName = fullName,
                    addressRecipientPhone = phoneNumber,
                    addressRecipientFullAddress = fullAddress,
                    addressRecipientProvince = province,
                    addressRecipientPostalCode = postalCode
                )

                if (editAddress == null) {
                    viewModel.addAddress(address) {
                        showToastAndFinish("Address added successfully")
                    }
                } else {
                    viewModel.updateAddress(address) {
                        showToastAndFinish("Address updated successfully")
                    }
                }
            }
        }

        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.errorState.observe(this) { errorMessage ->
            errorMessage?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showToastAndFinish(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        val resultIntent = Intent()
        resultIntent.putExtra("address_added", true)
        setResult(Activity.RESULT_OK, resultIntent)
        finish()
    }
}