package com.azizahfzahrr.eleccart.presentation.view.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.GetCredentialException
import androidx.lifecycle.lifecycleScope
import com.azizahfzahrr.eleccart.presentation.view.mainactivity.MainActivity
import com.azizahfzahrr.eleccart.data.source.local.PreferencedDataStore
import com.azizahfzahrr.eleccart.databinding.ActivityLoginBinding
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth

    @Inject
    lateinit var preferenceDataStore: PreferencedDataStore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        lifecycleScope.launch {
            if (preferenceDataStore.isUserLoggedIn()) {
                navigateToMain()
            }
        }

        binding.cvGoogleLogin.setOnClickListener {
            signInWithCredentialManager()
        }
    }

    private fun signInWithCredentialManager() {
        binding.loadingLayout.isVisible = true
        binding.ivGoogleSignin.isVisible = false
        binding.tvSignInWithGoogle.isVisible = false

        val credentialManager = CredentialManager.create(this)

        val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId("990817310007-uvp04lq7hgnvd0fn3ukfidti6i2sovaj.apps.googleusercontent.com")
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        lifecycleScope.launch {
            try {
                val result: GetCredentialResponse = credentialManager.getCredential(
                    request = request,
                    context = this@LoginActivity
                )
                handleSignIn(result)
            } catch (e: GetCredentialException) {
                Log.d("Error", e.message.toString())
                resetLoginState()
            }
        }
    }

    private fun handleSignIn(result: GetCredentialResponse) {
        when (val credential = result.credential) {
            is CustomCredential -> {
                if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    try {
                        val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                        firebaseAuthWithGoogle(googleIdTokenCredential.idToken)
                    } catch (e: Exception) {
                        Log.e("TAG", "Error parsing Google ID token", e)
                        resetLoginState()
                    }
                } else {
                    Log.e("TAG", "Unexpected credential type")
                    resetLoginState()
                }
            }
            else -> {
                Log.e("TAG", "Unexpected credential type")
                resetLoginState()
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String?) {
        if (idToken != null) {
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            auth.signInWithCredential(credential)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        lifecycleScope.launch {
                            preferenceDataStore.setUserLoggedIn(true)
                            navigateToMain()
                        }
                    } else {
                        Log.w("TAG", "signInWithCredential:failure", task.exception)
                        Toast.makeText(this@LoginActivity, "Login Failed! Check again your connection", Toast.LENGTH_SHORT).show()
                        resetLoginState()
                    }
                }
        } else {
            Log.e("TAG", "ID Token is null")
            resetLoginState()
        }
    }

    private fun resetLoginState() {
        binding.loadingLayout.isVisible = false
        binding.ivGoogleSignin.isVisible = true
        binding.tvSignInWithGoogle.isVisible = true
    }

    private fun navigateToMain() {
        val intent = Intent(this@LoginActivity, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}