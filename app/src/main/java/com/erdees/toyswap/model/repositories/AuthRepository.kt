package com.erdees.toyswap.model.repositories

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import com.erdees.toyswap.model.Registration
import com.erdees.toyswap.view.activities.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider

class AuthRepository(private val application: Application) {

    private var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val userLiveData: MutableLiveData<FirebaseUser> = MutableLiveData<FirebaseUser>()
    private val isUserLoggedOutLiveData: MutableLiveData<Boolean> = MutableLiveData<Boolean>()

    
    
    init {
        if (firebaseAuth.currentUser != null) {
            userLiveData.postValue(firebaseAuth.currentUser)
            isUserLoggedOutLiveData.postValue(false)
        }
    }

     fun updateUserLiveData(){
        if (firebaseAuth.currentUser != null) {
            userLiveData.postValue(firebaseAuth.currentUser)
            isUserLoggedOutLiveData.postValue(false)
        }
        else isUserLoggedOutLiveData.postValue(true)
    }

    fun getUserLiveData(): MutableLiveData<FirebaseUser> {
        return userLiveData
    }

    fun getIsUserLoggedOutLiveData(): MutableLiveData<Boolean> {
        return isUserLoggedOutLiveData
    }


      fun registerWithPassword(registration: Registration) {
        if (!registration.isLegit()) {
            Toast.makeText(application.applicationContext, registration.errorMessage, Toast.LENGTH_SHORT).show()
        } else signUpWithEmail(registration)
    }

     private  fun signUpWithEmail(registration: Registration) {
        firebaseAuth.createUserWithEmailAndPassword(registration.mail, registration.password)
            .addOnCompleteListener(ContextCompat.getMainExecutor(application)) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(LoginActivity.TAG, "createUserWithEmail:success")
                    updateUserLiveData()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(LoginActivity.TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        application.applicationContext, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

     fun loginWithPassword(email: String, password: String) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(ContextCompat.getMainExecutor(application)) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(LoginActivity.TAG, "signInWithEmail:success")
                    updateUserLiveData()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(LoginActivity.TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(
                        application.applicationContext, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(ContextCompat.getMainExecutor(application)) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(LoginActivity.TAG, "signInWithCredential:success")
                    updateUserLiveData()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(LoginActivity.TAG, "signInWithCredential:failure", task.exception)
                }
            }

    }

    fun signOut(){
        firebaseAuth.signOut()
        updateUserLiveData()
    }

}