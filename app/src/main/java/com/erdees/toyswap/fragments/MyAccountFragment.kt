package com.erdees.toyswap.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.erdees.toyswap.Constants
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

        binding.profilePic.setAvatar()
        setAddressAccordingly()


        binding.changeAddressBtn.setOnClickListener {
            val changeAddressDialog = ChangeAddressDialog(requireContext(),this)
            changeAddressDialog.show(childFragmentManager, ChangeAddressDialog.TAG)
        }

        binding.logoutButton.setOnClickListener {
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

    private fun ImageView.setAvatar(){
    userDocRef.get().addOnSuccessListener {
        val thisUserProfilePictureUrl =  it["avatar"]
        if(thisUserProfilePictureUrl != "") {
            Glide.with(requireActivity())
                .load(thisUserProfilePictureUrl)
                .centerCrop()
                .into(this)

            binding.profilePicButton.text = "Change Picture"
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
            if(street.isNullOrEmpty() && postCode.isNullOrEmpty() && city.isNullOrEmpty()) binding.addressTV.text = "You have not provided address."
            else setFields(street,postCode,city)
        }
    }

    private fun setFields(street: String, postCode: String, city: String){
        binding.addressTV.text = street
        binding.addressPostCodeTV.text = postCode
        binding.addressCityTV.text = city
    }

    private fun ImageView.setAnonProfilePicture(){
        Glide.with(requireActivity())
            .load(Constants.NO_PROFILE_PICTURE_URL)
            .centerCrop()
            .into(this)

        binding.profilePicButton.text = "Add Picture"
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun OnCloseDialog() {
        setAddressAccordingly()
    }

}