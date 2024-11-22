package com.azizahfzahrr.eleccart.presentation.view.address

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.azizahfzahrr.eleccart.R
import com.azizahfzahrr.eleccart.data.source.local.AddressEntity
import com.azizahfzahrr.eleccart.databinding.ActivityChooseAddressBinding
import com.azizahfzahrr.eleccart.presentation.adapter.ItemAddressAdapter
import com.azizahfzahrr.eleccart.presentation.listener.ItemAddressListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChooseAddressActivity : AppCompatActivity(), ItemAddressListener, ItemAddressAdapter.SelectedAddressListener {

    private lateinit var binding: ActivityChooseAddressBinding
    private val viewModel: ChooseAddressViewModel by viewModels()
    private lateinit var adapter: ItemAddressAdapter
    private var selectedAddress: AddressEntity? = null

    private val addAddressResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            viewModel.loadAddress()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChooseAddressBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        observeAddressList()

        binding.ivLeftArrowChooseAddress.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.btnAddAddress.setOnClickListener {
            val intent = Intent(this@ChooseAddressActivity, CreateAddressActivity::class.java)
            addAddressResult.launch(intent)
        }

        binding.btnConfirmAddress.isEnabled = false

        binding.btnConfirmAddress.setOnClickListener {
            if (selectedAddress != null) {
                Toast.makeText(this, "Address selected", Toast.LENGTH_SHORT).show()

                val intent = Intent().apply {
                    putExtra("selected_address", selectedAddress)
                }
                setResult(Activity.RESULT_OK, intent)
                finish()
            } else {
                Toast.makeText(this, "Please select an address", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.loadAddress()
    }

    private fun observeAddressList() {
        viewModel.listAddress.observe(this) { addressList ->
            adapter.updateDataAddress(addressList)
            val selectedAddress = adapter.getSelectedAddress()
            onAddressSelected(selectedAddress)
        }
    }

    private fun setupRecyclerView() {
        adapter = ItemAddressAdapter(emptyList(), this, this)
        binding.rvChooseAddress.layoutManager = LinearLayoutManager(this)
        binding.rvChooseAddress.adapter = adapter
    }

    override fun onAddressSelected(address: AddressEntity?) {
        selectedAddress = address
        binding.btnConfirmAddress.isEnabled = address != null

        val background = if (address == null){
            ContextCompat.getColor(this, R.color.grey_200)
        }else{
            ContextCompat.getColor(this, R.color.color_primary)
        }
        binding.btnConfirmAddress.setCardBackgroundColor(background)
    }

    override fun onDelete(address: AddressEntity) {
        viewModel.deleteAddress(address) {
            Toast.makeText(this, "Address deleted successfully", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onEdit(address: AddressEntity) {
        val intent = Intent(this@ChooseAddressActivity, CreateAddressActivity::class.java)
        intent.putExtra("addressToEdit", address)
        startActivity(intent)
    }
}
