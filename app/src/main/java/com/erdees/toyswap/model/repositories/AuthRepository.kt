package com.erdees.toyswap.model.repositories

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import com.erdees.toyswap.model.Address
import com.erdees.toyswap.model.Registration
import com.erdees.toyswap.view.activities.LoginActivity
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AuthRepository(private val application: Application) {

    private var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    private val userLiveData: MutableLiveData<FirebaseUser?> = MutableLiveData<FirebaseUser?>()
    private val isUserLoggedOutLiveData: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    private val userAddressLiveData: MutableLiveData<Address> = MutableLiveData<Address>()
    private val docRefLiveData: MutableLiveData<DocumentReference> = MutableLiveData<DocumentReference>()

    init {
        updateUserLiveData()
    }

    fun updateUserLiveData() {
        Log.i("TEST", " THIS THING IS CALLED DOES IT RUIN IT?")
        if (firebaseAuth.currentUser != null) {
            userLiveData.postValue(firebaseAuth.currentUser)
            isUserLoggedOutLiveData.postValue(false)
            val docRef =  Firebase.firestore.collection("users").document(firebaseAuth.currentUser!!.uid)
            docRefLiveData.postValue(docRef)
            getAddress(docRef)

        } else{
            isUserLoggedOutLiveData.postValue(true)
            userLiveData.value = null
        }
    }

    fun getUserLiveData(): MutableLiveData<FirebaseUser?> {
        return userLiveData
    }

    fun getIsUserLoggedOutLiveData(): MutableLiveData<Boolean> {
        return isUserLoggedOutLiveData
    }


    fun registerWithPassword(registration: Registration) {
        if (!registration.isLegit()) {
            Toast.makeText(
                application.applicationContext,
                registration.errorMessage,
                Toast.LENGTH_SHORT
            ).show()
        } else signUpWithEmail(registration)
    }

    private fun signUpWithEmail(registration: Registration) {
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

    fun signOut() {
        firebaseAuth.signOut()
        updateUserLiveData()
    }

    fun updateAddress( address: Address): Task<Void>? {
        with(address) {
            setUserAddress(address)
            return docRefLiveData.value?.update(
                "addressStreet", street,
                "addressPostCode", postCode,
                "addressCity", city
            )
        }
    }

    private fun getAddress(docRef: DocumentReference){
        docRef.get().addOnSuccessListener {
            val street = (it["addressStreet"].toString())
            val postCode = (it["addressPostCode"].toString())
            val city = (it["addressCity"].toString())
            val address = Address(street, postCode, city)
            setUserAddress(address)
        }
    }

    private fun setUserAddress(address: Address) {
        Log.i("TEST!!!","ADDRESS UPDATEED FOR ! ::: $address")
        userAddressLiveData.value = address
        Log.i("TEST!!!","ADDRESS UPDATEED FOR ! ::: ${userAddressLiveData.value}")

    }

    fun getUserAddressLiveData(): MutableLiveData<Address> {
       return userAddressLiveData
    }


    /**Singleton object */
    companion object {
        @Volatile private var instance: AuthRepository? = null
        fun getInstance(application: Application) =
            instance ?: synchronized(this) {
                instance
                    ?: AuthRepository(application).also { instance = it }
            }
    }

}