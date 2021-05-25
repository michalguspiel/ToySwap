package com.erdees.toyswap.activities.loginActivity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.erdees.toyswap.Constants.RC_SIGN_IN
import com.erdees.toyswap.R
import com.erdees.toyswap.Utils.makeGone
import com.erdees.toyswap.Utils.makeToast
import com.erdees.toyswap.Utils.makeVisible
import com.erdees.toyswap.activities.MainActivity
import com.erdees.toyswap.databinding.WelcomeActivityBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: WelcomeActivityBinding
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var auth: FirebaseAuth
    private lateinit var view: View

    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = WelcomeActivityBinding.inflate(layoutInflater)
        view = binding.root
        setContentView(view)
        checkSignIn() // initially screen starts with checked sign in value


        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        // Initialize Firebase Auth
        auth = Firebase.auth

        binding.signAnnon.setOnClickListener {
            continueWithoutLogging()
        }
        binding.signWithEmailBtn.setOnClickListener {
            signInWithMail()
        }
        binding.signUpWithEmailBtn.setOnClickListener {
            val registration = Registration(binding.signUpPasswordInput.text.toString(),
                binding.signUpConfirmPasswordInput.text.toString(),
                binding.signUpWithEmailInput.text.toString())
            if(!registration.isLegit()){
                view.makeToast(registration.errorMessage)
            }
            else signUp(registration)
        }
        binding.signWithGoogleBtn.setOnClickListener {
            signInWithGoogle()
        }
        binding.welcomeSignInSwitch.setOnClickListener {
            checkSignIn()
        }
        binding.welcomeSignUpSwitch.setOnClickListener {
            checkSignUp()
        }

    }

    private fun checkSignIn(){
        binding.welcomeSignInRowActive.makeVisible()
        binding.welcomeSignInRowInactive.makeGone()
        binding.welcomeSignUpRowActive.makeGone()
        binding.welcomeSignUpRowInactive.makeVisible()
        binding.welcomeSignInSwitch.setTextColor(ContextCompat.getColor(this,R.color.colorPrimary))
        binding.welcomeSignUpSwitch.setTextColor(ContextCompat.getColor(this,R.color.light_gray))
        binding.welcomeSignUpLayout.makeGone()
        binding.welcomeSignInLayout.makeVisible()

    }

    private fun checkSignUp(){
        binding.welcomeSignInRowActive.makeGone()
        binding.welcomeSignInRowInactive.makeVisible()
        binding.welcomeSignUpRowActive.makeVisible()
        binding.welcomeSignUpRowInactive.makeGone()
        binding.welcomeSignInSwitch.setTextColor(ContextCompat.getColor(this,R.color.light_gray))
        binding.welcomeSignUpSwitch.setTextColor(ContextCompat.getColor(this,R.color.colorPrimary))
        binding.welcomeSignUpLayout.makeVisible()
        binding.welcomeSignInLayout.makeGone()
    }

    private fun updateUI(user: FirebaseUser?){
        if(user == null) return
        else openMainActivity()

    }

    private fun openMainActivity(){
        val mainActivity = Intent(this, MainActivity::class.java)
            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(mainActivity)
    }

    private fun signUp(registration: Registration){
        auth.createUserWithEmailAndPassword(registration.mail,registration.password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success")
                    checkSignIn()
                    val user = auth.currentUser
                  openMainActivity()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

    }

    private fun signInWithMail() {
        auth.signInWithEmailAndPassword(binding.emailInput.text.toString(), binding.passwordInput.text.toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success")
                    val user = auth.currentUser
                    openMainActivity()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                    updateUI(null)
                }
            }
    }

    private fun signInWithGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }


    private fun continueWithoutLogging() {
    openMainActivity()
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    updateUI(null)
                }
            }
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
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e)
            }
        }
    }

    companion object {
        const val TAG = "WelcomeActivity"
    }

}