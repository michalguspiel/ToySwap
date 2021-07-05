package com.erdees.toyswap.model.firebaseAuth

import android.app.Application
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import com.erdees.toyswap.model.Registration
import com.erdees.toyswap.model.models.Address
import com.erdees.toyswap.model.models.user.ToySwapUser
import com.google.android.gms.tasks.Task
import com.google.firebase.Timestamp
import com.google.firebase.auth.*
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask

class AuthUserDao(private val application: Application) {

    private var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firebaseUserLiveData: MutableLiveData<FirebaseUser?> =
        MutableLiveData<FirebaseUser?>()
    private val isUserLoggedOutLiveData: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    private val userDocRefLiveData: MutableLiveData<DocumentReference> = MutableLiveData<DocumentReference>()
    private val userPublicInfoDocRefLiveData: MutableLiveData<DocumentReference> = MutableLiveData<DocumentReference>()

    private val toySwapUserLiveData: MutableLiveData<ToySwapUser?> = MutableLiveData<ToySwapUser?>()

    private val userAuthProviderTokenLiveData : MutableLiveData<String> = MutableLiveData<String>()

    private val storage = FirebaseStorage.getInstance()
    private val storageRef = storage.reference

    private fun profilePicImageRef() = storageRef.child("profilePics/${firebaseAuth.currentUser?.uid}_profile_pic.jpg")

    init {
        updateUserLiveData()
    }

    private fun saveFirebaseUserAsClientUser(docRef: DocumentReference?) {
        if (docRef != null) {
            docRef.get().addOnSuccessListener {
                val name: String = it["firstName"].toString()
                val lastName: String = it["lastName"].toString()
                val email: String = it["emailAddress"].toString()
                val points: Long = it["points"].toString().toLong()
                val reputation: Long = it["reputation"].toString().toLong()
                val avatar: String = it["avatar"].toString()
                val addressCity: String = it["addressCity"].toString()
                val addressPostCode: String = it["addressPostCode"].toString()
                val addressStreet: String = it["addressStreet"].toString()
                val accCreationTimeStamp : Timestamp = it["accCreationTimestamp"] as Timestamp

                val thisSessionUser =
                    ToySwapUser(
                        name,
                        lastName,
                        email,
                        points,
                        reputation,
                        avatar,
                        addressCity,
                        addressPostCode,
                        addressStreet,
                        accCreationTimeStamp
                    )
                Log.i(TAG, "this session user saved ! $thisSessionUser")
                toySwapUserLiveData.postValue(thisSessionUser)
            }
        }
        else {
            toySwapUserLiveData.postValue(null)

        }
    }

    private fun FirebaseUser.updateDocumentReferences(){
        val userDocRef =
            Firebase.firestore.collection("users").document(this.uid)
        val userPublicInfoDocRef = Firebase.firestore.collection("usersPublicInfo").document(this.uid)
        userDocRefLiveData.value = (userDocRef)
        userPublicInfoDocRefLiveData.value = (userPublicInfoDocRef)
        saveFirebaseUserAsClientUser(userDocRef)

    }

    private fun updateUserLiveData() {
        val fireBaseUser = firebaseAuth.currentUser
        if (fireBaseUser != null) {
            firebaseUserLiveData.postValue(firebaseAuth.currentUser)
            fireBaseUser.getIdToken(false).addOnSuccessListener { userAuthProviderTokenLiveData.postValue(it.signInProvider) }
            isUserLoggedOutLiveData.postValue(false)
            fireBaseUser.updateDocumentReferences()
            saveFirebaseUserAsClientUser(userDocRefLiveData.value)
        } else {
            isUserLoggedOutLiveData.value = true
            firebaseUserLiveData.value = null
            toySwapUserLiveData.value = null
            saveFirebaseUserAsClientUser(null)
        }
    }


    fun getClientUserLiveData(): MutableLiveData<ToySwapUser?> {
        return toySwapUserLiveData
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


     fun signUpWithEmail(registration: Registration) : Task<AuthResult> {
        return firebaseAuth.createUserWithEmailAndPassword(registration.mail, registration.password)
            .addOnCompleteListener(ContextCompat.getMainExecutor(application)) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success")
                    if(firebaseAuth.currentUser != null) checkIfDocumentExistAndIfSoContinue()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        application.applicationContext, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    fun loginWithPassword(email: String, password: String) :Task <AuthResult> {
      return  firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(ContextCompat.getMainExecutor(application)) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success")
                    if(firebaseAuth.currentUser != null) checkIfDocumentExistAndIfSoContinue()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(
                        application.applicationContext, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    fun firebaseAuthWithGoogle(idToken: String) : Task<AuthResult> {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
       return firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(ContextCompat.getMainExecutor(application)) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    if(firebaseAuth.currentUser != null) checkIfDocumentExistAndIfSoContinue()

                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                }
            }

    }

    private fun checkIfDocumentExistAndIfSoContinue(): Task<DocumentSnapshot> {
        val docRef =
            Firebase.firestore.collection("users").document(firebaseAuth.currentUser!!.uid)
        return docRef.get().addOnSuccessListener {
            if (!it.exists()) checkIfDocumentExistAndIfSoContinue()
            else{
                updateUserLiveData()
            }
        }
    }

    fun signOut() {
        firebaseAuth.signOut()
        updateUserLiveData()
    }

    fun updateAddress(address: Address): Task<Void>? {
        with(address) {
            return userDocRefLiveData.value?.update(
                "addressStreet", street,
                "addressPostCode", postCode,
                "addressCity", city
            )?.addOnSuccessListener {
                updateClientAddressLiveData(address)
                updatePublicInfoAboutAddress(city)
            }
        }
    }

    private fun updatePublicInfoAboutAddress(city : String ): Task<Void>?{
        return userPublicInfoDocRefLiveData.value?.update(
            "addressCity", city
        )
    }

    private fun updateClientAddressLiveData(address: Address) {
        val thisClient = toySwapUserLiveData.value
        if (thisClient != null) {
            val updatedClient = ToySwapUser(
                thisClient.firstName,
                thisClient.lastName,
                thisClient.emailAddress,
                thisClient.points,
                thisClient.reputation,
                thisClient.avatar,
                address.city,
                address.postCode,
                address.street,
                thisClient.accCreationTimeStamp
            )
        toySwapUserLiveData.postValue(updatedClient)
        }
    else Log.i(TAG,"ERROR UPDATING CLIENT ADDRESS!")
    }

    private fun updateClientAvatar(uri: String) {
        val thisClient = toySwapUserLiveData.value
        if (thisClient != null) {
            val updatedClient = ToySwapUser(
                thisClient.firstName,
                thisClient.lastName,
                thisClient.emailAddress,
                thisClient.points,
                thisClient.reputation,
                uri,
                thisClient.addressCity,
                thisClient.addressPostCode,
                thisClient.addressStreet,
                thisClient.accCreationTimeStamp
            )
            toySwapUserLiveData.postValue(updatedClient)
        }
        else Log.i(TAG,"ERROR UPDATING CLIENT AVATAR!")
    }

    private fun updatePublicAvatarInfo(uri : String) {
        userPublicInfoDocRefLiveData.value?.update("avatar",uri)
    }

    fun updateFirebaseUserAvatar(uri: String): Task<Void>?{
        return userDocRefLiveData.value?.update("avatar",uri)?.addOnSuccessListener {
            updatePublicAvatarInfo(uri)
            updateClientAvatar(uri)
        }
        }

    private fun updateClientName(firstName: String, lastName: String){
        val thisClient = toySwapUserLiveData.value
        if(thisClient != null){
            val updatedClient = ToySwapUser(
                firstName,
                lastName,
                thisClient.emailAddress,
                thisClient.points,
                thisClient.reputation,
                thisClient.avatar,
                thisClient.addressCity,
                thisClient.addressPostCode,
                thisClient.addressStreet,
                thisClient.accCreationTimeStamp
            )
            toySwapUserLiveData.postValue(updatedClient)
        }
    }

    fun updatePublicInfoAboutName(firstName: String){
        userPublicInfoDocRefLiveData.value?.update("firstName",firstName)
    }

    fun updateNames(firstName: String,lastName: String): Task<Void>? {
        return userDocRefLiveData.value?.update("firstName",firstName,"lastName",lastName)
            ?.addOnSuccessListener {
                updateClientName(firstName,lastName)
                updatePublicInfoAboutName(firstName)
            }

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
        const val TAG = "AuthUserDao"
        @Volatile
        private var instance: AuthUserDao? = null
        fun getInstance(application: Application) =
            instance ?: synchronized(this) {
                instance
                    ?: AuthUserDao(application).also { instance = it }
            }
    }
}