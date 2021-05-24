package com.erdees.toyswap.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.erdees.toyswap.Constants
import com.erdees.toyswap.Utils.makeGone
import com.erdees.toyswap.Utils.makeVisible
import com.erdees.toyswap.activities.MainActivity
import com.erdees.toyswap.databinding.FragmentMyAccountBinding
import com.erdees.toyswap.fragments.dialogs.ChangeAddressDialog
import com.erdees.toyswap.fragments.dialogs.DialogListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class MyAccountFragment : Fragment(),DialogListener {

    private var _binding: FragmentMyAccountBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth
    private lateinit var db : FirebaseFirestore
    private lateinit var user : FirebaseUser
    private lateinit var userDocRef : DocumentReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentMyAccountBinding.inflate(inflater, container, false)
        val view = binding.root

        auth = Firebase.auth
        db  = Firebase.firestore
        user = auth.currentUser!!
        userDocRef = db.collection("users").document(user.uid)

        setUserNameAndMail()
        binding.profilePic.setAvatar()
        setAddressAccordingly()
        setButtonsAccordingly()

        binding.changeAddressBtn.setOnClickListener {
            val changeAddressDialog = ChangeAddressDialog(this)
            changeAddressDialog.show(childFragmentManager, ChangeAddressDialog.TAG)
        }

        binding.logoutBtn.setOnClickListener {
            auth.signOut()
            restartActivity()
        }

        return view
    }

    companion object {
        fun newInstance(): MyAccountFragment = MyAccountFragment()
        const val TAG = "MyAccountFragment"
    }

    private fun restartActivity() {
        val mainActivity = Intent(context, MainActivity::class.java)
        startActivity(mainActivity)
    }

    private fun setButtonsAccordingly(){
       if(user.getIdToken(false).result?.signInProvider == "google.com") binding.changePasswordBtn.makeGone()
       else binding.changePasswordBtn.makeVisible()
    }

    private fun setUserNameAndMail(){
        userDocRef.get().addOnSuccessListener {
            val userName = it["name"].toString()
            val userMail = it["emailAddress"].toString()
            if(!userName.isNullOrBlank())binding.accountFullNameTV.text = userName
            else binding.accountFullNameTV.text = ""

            binding.accountEmailTV.text = userMail
        }
    }

    private fun ImageView.setAvatar(){
    userDocRef.get().addOnSuccessListener {
        val thisUserProfilePictureUrl =  it["avatar"]
        if(thisUserProfilePictureUrl != "") {
            Glide.with(requireActivity())
                .load(thisUserProfilePictureUrl)
                .centerCrop()
                .into(this)

            binding.changePictureBtn.text = "Change Picture"
        }
        else this.setAnonProfilePicture()
    }
        .addOnFailureListener {
            this.setAnonProfilePicture()
        }
    }

    private fun setAddressAccordingly(){
        userDocRef.get().addOnSuccessListener {
            val street = (it["addressStreet"].toString())
            val postCode = (it["addressPostCode"].toString())
            val city = (it["addressCity"].toString())
            Log.i(TAG," $street    ,    $postCode,    $city")
            if(street.isNullOrEmpty() && postCode.isNullOrEmpty() && city.isNullOrEmpty()) setEmptyFields()
            else setFields(street,postCode,city)
        }
    }

    private fun setEmptyFields(){
        binding.addressTV.text = "You have not provided address."
        binding.addressPostCodeTV.makeGone()
        binding.addressCityTV.makeGone()
    }

    private fun setFields(street: String, postCode: String, city: String){
        binding.addressTV.text = street
        binding.addressPostCodeTV.text = postCode
        binding.addressCityTV.text = city
        binding.addressPostCodeTV.makeVisible()
        binding.addressCityTV.makeVisible()
    }

    private fun ImageView.setAnonProfilePicture(){
        Glide.with(requireActivity())
            .load(Constants.NO_PROFILE_PICTURE_URL)
            .centerCrop()
            .into(this)

        binding.changePictureBtn.text = "Add Picture"
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun OnCloseDialog() {
        setAddressAccordingly()
    }

}