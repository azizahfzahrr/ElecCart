package com.azizahfzahrr.eleccart.presentation.view.address

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
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

    private val addAddressResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val addressAdded = result.data?.getBooleanExtra("address_added", false) ?: false
            if (addressAdded) {
                viewModel.loadAddress()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChooseAddressBinding.inflate(layoutInflater)
        setContentView(binding.root)

        addressRecyclerView()
        addressObserve()

        binding.ivLeftArrowChooseAddress.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.btnAddAddress.setOnClickListener {
            val intent = Intent(this@ChooseAddressActivity, CreateAddressActivity::class.java)
            addAddressResultLauncher.launch(intent)
        }

        viewModel.loadAddress()
    }

    private fun addressObserve() {
        viewModel.listAddress.observe(this) { addressList ->
            adapter.updateDataAddress(addressList)
        }

    }

    private fun addressRecyclerView() {
        adapter = ItemAddressAdapter(emptyList(), this, this)
        binding.rvChooseAddress.layoutManager = LinearLayoutManager(this@ChooseAddressActivity)
        binding.rvChooseAddress.adapter = adapter
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

    override fun onAddressSelected(address: AddressEntity) {
        val intent = Intent().apply {
            putExtra("selected_address", address)
        }
        setResult(Activity.RESULT_OK, intent)
        finish()
    }
}