package com.erdees.toyswap.model.firebase

import android.app.Application
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import com.erdees.toyswap.model.Address
import com.erdees.toyswap.model.Registration
import com.erdees.toyswap.view.activities.LoginActivity
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask

class AuthDao(private val application: Application) {

    private var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firebaseUserLiveData: MutableLiveData<FirebaseUser?> =
        MutableLiveData<FirebaseUser?>()
    private val isUserLoggedOutLiveData: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    private val docRefLiveData: MutableLiveData<DocumentReference> =
        MutableLiveData<DocumentReference>()

    private val clientUserLiveData: MutableLiveData<ClientUser?> = MutableLiveData<ClientUser?>()

    private val userAuthProviderTokenLiveData : MutableLiveData<String> = MutableLiveData<String>()

    private val storage = FirebaseStorage.getInstance()
    private val storageRef = storage.reference

    private fun profilePicImageRef() = storageRef.child("profilePics/${firebaseAuth.currentUser?.uid}_profile_pic.jpg")

    init {
        updateUserLiveData()
    }

    private fun saveFirebaseUserAsClientUser(docRef: DocumentReference) {
        docRef.get().addOnSuccessListener {
            val name: String = it["firstName"].toString()
            val lastName : String = it["lastName"].toString()
            val email: String = it["emailAddress"].toString()
            val points: Long = it["points"].toString().toLong()
            val avatar: String = it["avatar"].toString()
            val addressCity: String = it["addressCity"].toString()
            val addressPostCode: String = it["addressPostCode"].toString()
            val addressStreet: String = it["addressStreet"].toString()

            setUserAddress(Address(addressStreet, addressPostCode, addressCity))

            val thisSessionUser =
                ClientUser(name,lastName, email, points, avatar, addressCity, addressPostCode, addressStreet)
            Log.i("TEST", "this session user saved ! $thisSessionUser")
            clientUserLiveData.postValue(thisSessionUser)
        }
    }

    private fun updateUserLiveData() {
        val fireBaseUser = firebaseAuth.currentUser
        Log.i("TEST", " THIS THING IS CALLED DOES IT RUIN IT?")
        if (fireBaseUser != null) {
            firebaseUserLiveData.postValue(firebaseAuth.currentUser)
            fireBaseUser.getIdToken(false).addOnSuccessListener { userAuthProviderTokenLiveData.postValue(it.signInProvider) }
            isUserLoggedOutLiveData.postValue(false)
            val docRef =
                Firebase.firestore.collection("users").document(fireBaseUser.uid)
            docRefLiveData.postValue(docRef)
            saveFirebaseUserAsClientUser(docRef)
        } else {
            isUserLoggedOutLiveData.postValue(true)
            firebaseUserLiveData.value = null
            clientUserLiveData.value = null
        }
    }


    fun getClientUserLiveData(): MutableLiveData<ClientUser?> {
        return clientUserLiveData
    }

    fun getUserLiveData(): MutableLiveData<FirebaseUser?> {
        return firebaseUserLiveData
    }

    fun getIsUserLoggedOutLiveData(): MutableLiveData<Boolean> {
        return isUserLoggedOutLiveData
    }

    fun getUserSignInProviderLiveData() :MutableLiveData<String>{
        return userAuthProviderTokenLiveData
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

    fun updateAddress(address: Address): Task<Void>? {
        with(address) {
            setUserAddress(address)
            return docRefLiveData.value?.update(
                "addressStreet", street,
                "addressPostCode", postCode,
                "addressCity", city
            )
        }
    }

    private fun setUserAddress(address: Address) {
        updateClientAddressLiveData(address)
    }

    private fun updateClientAddressLiveData(address: Address) {
        val thisClient = clientUserLiveData.value
        if (thisClient != null) {
            val updatedClient = ClientUser(
                thisClient.firstName,
                thisClient.lastName,
                thisClient.emailAddress,
                thisClient.points,
                thisClient.avatar,
                address.city,
                address.postCode,
                address.street
            )
        clientUserLiveData.postValue(updatedClient)
        }
    else Log.i("AuthDao","ERROR UPDATING CLIENT ADDRESS!")
    }

    private fun updateClientAvatar(uri: String) {
        val thisClient = clientUserLiveData.value
        if (thisClient != null) {
            val updatedClient = ClientUser(
                thisClient.firstName,
                thisClient.lastName,
                thisClient.emailAddress,
                thisClient.points,
                uri,
                thisClient.addressCity,
                thisClient.addressPostCode,
                thisClient.addressStreet
            )
            clientUserLiveData.postValue(updatedClient)
        }
        else Log.i("AuthDao","ERROR UPDATING CLIENT AVATAR!")
    }

    fun updateFirebaseUserAvatar(uri: String): Task<Void>?{
        return docRefLiveData.value?.update("avatar",uri)?.addOnSuccessListener { updateClientAvatar(uri)}
        }

    fun updateClientName(firstName: String,lastName: String){
        val thisClient = clientUserLiveData.value
        if(thisClient != null){
            val updatedClient = ClientUser(
                firstName,
                lastName,
                thisClient.emailAddress,
                thisClient.points,
                thisClient.avatar,
                thisClient.addressCity,
                thisClient.addressPostCode,
                thisClient.addressStreet
            )
            clientUserLiveData.postValue(updatedClient)
        }
    }

    fun updateNames(firstName: String,lastName: String): Task<Void>? {
        return docRefLiveData.value?.update("firstName",firstName,"lastName",lastName)
            ?.addOnSuccessListener { updateClientName(firstName,lastName) }

    }


    fun reAuthenticate(cred: AuthCredential): Task<Void>? {
        return firebaseAuth.currentUser?.reauthenticate(cred)
    }

    fun changePassword(registration: Registration): Task<Void>? {
        return firebaseAuth.currentUser?.updatePassword(registration.password)
    }

    fun deleteCurrentAvatar(): Task<Void> {
        return profilePicImageRef().delete()
    }

    fun uploadNewAvatar(imageUri: Uri) : UploadTask{
     return  profilePicImageRef().putFile(imageUri)
    }

    fun getNewAvatarUrl() : Task<Uri> {
       return profilePicImageRef().downloadUrl
    }




    /**SINGLETON*/
    companion object {
        @Volatile
        private var instance: AuthDao? = null
        fun getInstance(application: Application) =
            instance ?: synchronized(this) {
                instance
                    ?: AuthDao(application).also { instance = it }
            }
    }
}