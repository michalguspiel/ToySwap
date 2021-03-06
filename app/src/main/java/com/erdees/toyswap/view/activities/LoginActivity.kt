package com.erdees.toyswap.view.activities

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.erdees.toyswap.model.Constants.RC_SIGN_IN
import com.erdees.toyswap.R
import com.erdees.toyswap.model.utils.Utils.makeGone
import com.erdees.toyswap.model.utils.Utils.makeVisible
import com.erdees.toyswap.databinding.LoginActivityBinding
import com.erdees.toyswap.model.utils.Registration
import com.erdees.toyswap.viewModel.LoginActivityViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException


class LoginActivity : AppCompatActivity() {

    private lateinit var binding: LoginActivityBinding
    private lateinit var view: View
    private lateinit var viewModel: LoginActivityViewModel

    private lateinit var alertDialog: AlertDialog


    private val gso by lazy {
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
    }
    private val googleSignInClient by lazy {
        GoogleSignIn.getClient(this, gso)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = LoginActivityBinding.inflate(layoutInflater)
        view = binding.root
        setContentView(view)
        checkSignIn() // initially screen starts with checked sign in value
        viewModel = ViewModelProvider(this).get(LoginActivityViewModel::class.java)


        viewModel.clientUserLiveData.observe(this,{userInClient ->
            if(userInClient != null){
                endLoading()
                openMainActivity()
            }
        })

        binding.signWithEmailBtn.setOnClickListener {
            showLoading()
            val email = binding.emailInput.text.toString()
            val password =  binding.passwordInput.text.toString()
            if(email.isEmpty() || password.isEmpty()) return@setOnClickListener
            viewModel.loginWithEmail(
                email,
                password
            ).addOnFailureListener { endLoading() }
        }
        binding.signUpWithEmailBtn.setOnClickListener {
            showLoading()
            val registration = Registration(
                binding.signUpPasswordInput.text.toString(),
                binding.signUpConfirmPasswordInput.text.toString(),
                binding.signUpWithEmailInput.text.toString()
            )
            if(registration.isLegit()) viewModel.registerWithEmail(registration).addOnFailureListener { endLoading() }
            else{
                endLoading()
                Toast.makeText(
                    this,
                    registration.errorMessage,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        binding.signWithGoogleBtn.setOnClickListener {
            showLoading()
            signInWithGoogle()
        }
        binding.welcomeSignInSwitch.setOnClickListener {
            checkSignIn()
        }
        binding.welcomeSignUpSwitch.setOnClickListener {
            checkSignUp()
        }

    }

    private fun checkSignIn() {
        binding.welcomeSignInRowActive.makeVisible()
        binding.welcomeSignInRowInactive.makeGone()
        binding.welcomeSignUpRowActive.makeGone()
        binding.welcomeSignUpRowInactive.makeVisible()
        binding.welcomeSignInSwitch.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary))
        binding.welcomeSignUpSwitch.setTextColor(ContextCompat.getColor(this, R.color.light_gray))
        binding.welcomeSignUpLayout.makeGone()
        binding.welcomeSignInLayout.makeVisible()

    }

    private fun checkSignUp() {
        binding.welcomeSignInRowActive.makeGone()
        binding.welcomeSignInRowInactive.makeVisible()
        binding.welcomeSignUpRowActive.makeVisible()
        binding.welcomeSignUpRowInactive.makeGone()
        binding.welcomeSignInSwitch.setTextColor(ContextCompat.getColor(this, R.color.light_gray))
        binding.welcomeSignUpSwitch.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary))
        binding.welcomeSignUpLayout.makeVisible()
        binding.welcomeSignInLayout.makeGone()
    }


    private fun openMainActivity() {
        val mainActivity = Intent(this, MainActivity::class.java)
            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(mainActivity)
    }


    private fun signInWithGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
                viewModel.signUpWithGoogle(account.idToken!!).addOnFailureListener { endLoading() }
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e)
            }
        }
    }

    private fun showLoading(){
        alertDialog = AlertDialog.Builder(this)
            .setCancelable(false)
            .setMessage("Loading...")
            .setView(ProgressBar(this))
            .show()

    }

    private fun endLoading(){
        alertDialog.dismiss()
    }

    companion object {
        const val TAG = "WelcomeActivity"

    }
}

