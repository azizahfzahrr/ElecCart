package com.azizahfzahrr.eleccart.presentation.view.profile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import com.azizahfzahrr.eleccart.LoginActivity
import com.azizahfzahrr.eleccart.R
import com.azizahfzahrr.eleccart.databinding.FragmentProfileBinding
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.user.observe(viewLifecycleOwner) { userInfo ->
            binding.tvNameUser.text = userInfo.name
            binding.tvEmailProfile.text = userInfo.email
            binding.tvPhoneProfile.text = userInfo.phone

            if (userInfo.imageUrl!= null) {
                Glide.with(requireContext())
                    .load(userInfo.imageUrl)
                    .circleCrop()
                    .into(binding.ivProfile)
            } else {
                binding.ivProfile.setImageResource(R.drawable.human)
            }
        }
        binding.ivArrowRightProfile.setOnClickListener {
            val firebaseUser = FirebaseAuth.getInstance().currentUser
            val photoUrl = firebaseUser?.photoUrl

            if (photoUrl != null) {
                Log.d("ProfileFragment", "Photo URL: $photoUrl")
            } else {
                Log.d("ProfileFragment", "Photo URL is null")
            }

            val intent = Intent(requireContext(), DetailProfileActivity::class.java).apply {
                putExtra("USER_PHOTO_URL", photoUrl.toString())
            }
            startActivity(intent)
        }

        binding.arrowLogout.setOnClickListener {
            showLogoutConfirmationDialog()
        }
    }

    private fun showLogoutConfirmationDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Logout")
        builder.setMessage("Are you sure you want to logout?")
        builder.setPositiveButton("Yes") { dialog, _ ->
            logoutUser()
            dialog.dismiss()
        }
        builder.setNegativeButton("No") { dialog, _ -> dialog.dismiss() }
        builder.create().show()
    }

    private fun logoutUser() {
        clearUserData()
        startActivity(Intent(requireContext(), LoginActivity::class.java))
        requireActivity().finish()
    }

    private fun clearUserData() {
        FirebaseAuth.getInstance().signOut()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}