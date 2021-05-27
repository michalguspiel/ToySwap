package com.erdees.toyswap.view.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.signature.ObjectKey
import com.erdees.toyswap.Constants
import com.erdees.toyswap.Utils.makeGone
import com.erdees.toyswap.Utils.makeVisible
import com.erdees.toyswap.databinding.FragmentMyAccountBinding
import com.erdees.toyswap.view.activities.MainActivity
import com.erdees.toyswap.view.fragments.dialogs.ChangeAddressDialog
import com.erdees.toyswap.view.fragments.dialogs.ReAuthenticateDialog
import com.erdees.toyswap.viewModel.MyAccountFragmentViewModel
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class MyAccountFragment : Fragment() {

    private var _binding: FragmentMyAccountBinding? = null
    private val binding get() = _binding!!

    private lateinit var db : FirebaseFirestore
    private  lateinit var user : FirebaseUser
    private lateinit var userDocRef : DocumentReference
    private lateinit var viewModel : MyAccountFragmentViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentMyAccountBinding.inflate(inflater, container, false)
        val view = binding.root
        viewModel = ViewModelProvider(this).get(MyAccountFragmentViewModel::class.java)
        db  = Firebase.firestore

        viewModel.userLiveData.observe(viewLifecycleOwner,{
            if(it != null) {
                user = it
                userDocRef = db.collection("users").document(user.uid)
                setUserNameAndMail()
                binding.profilePic.setAvatar()
                setButtonsAccordingly()
            }
        })

        viewModel.addressLiveData.observe(viewLifecycleOwner,{ address ->
            with (address){
                if(street.isEmpty() && postCode.isEmpty() && city.isEmpty()) setEmptyFields()
                else setFields(street,postCode,city)
            }
        })

        viewModel.isUserLoggedOutLiveData.observe(viewLifecycleOwner,{
            if(it) restartActivity()
        })


        binding.changeAddressBtn.setOnClickListener {
            val changeAddressDialog = ChangeAddressDialog()
            changeAddressDialog.show(childFragmentManager, ChangeAddressDialog.TAG)
        }

        binding.logoutBtn.setOnClickListener {
            viewModel.signOut()
        }

        binding.changePasswordBtn.setOnClickListener {
            openDialogToReAuthenticate()
        }

        return view
    }

    companion object {
        fun newInstance(): MyAccountFragment = MyAccountFragment()
        const val TAG = "MyAccountFragment"
    }

    private fun openDialogToReAuthenticate(){
    val reAuthDialog = ReAuthenticateDialog(false)
        reAuthDialog.show(childFragmentManager,ReAuthenticateDialog.TAG)
    }

    private fun restartActivity() {
        val mainActivity = Intent(context, MainActivity::class.java)
        startActivity(mainActivity)
    }

    private fun setButtonsAccordingly(){  // TODO THIS NEEDS TO BE STORED SOMEWHERE LOCALLY SO IT WORKS SMOOTHER PROBABLY WITH USER LIVE DATA .
        user.getIdToken(false).addOnSuccessListener { result ->
            if(result.signInProvider == "google.com")binding.changePasswordBtn.makeGone()
            else binding.changePasswordBtn.makeVisible()
        }
    }

    private fun setUserNameAndMail(){
        userDocRef.get().addOnSuccessListener {
            val userName = it["name"].toString()
            val userMail = it["emailAddress"].toString()
            if(userName.isNotBlank())binding.accountFullNameTV.text = userName
            else binding.accountFullNameTV.text = ""

            binding.accountEmailTV.text = userMail
        }
    }

    @SuppressLint("SetTextI18n")
    private fun ImageView.setAvatar(){
    userDocRef.get().addOnSuccessListener {
        val thisUserProfilePictureUrl =  it["avatar"].toString()

        if(thisUserProfilePictureUrl.isNotBlank() && thisUserProfilePictureUrl != "null") {
            Glide.with(requireActivity())
                .load(thisUserProfilePictureUrl)
                .centerCrop()
                .signature(ObjectKey(thisUserProfilePictureUrl))
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .into(this)

            binding.changePictureBtn.text = "Change Picture"
        }
        else this.setAnonProfilePicture()
    }
        .addOnFailureListener {
            this.setAnonProfilePicture()
        }
    }

    @SuppressLint("SetTextI18n")
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

    @SuppressLint("SetTextI18n")
    private fun ImageView.setAnonProfilePicture(){
        Log.i(TAG,"SettingAnonPic")
        Glide.with(requireActivity())
            .load(Constants.NO_PROFILE_PICTURE_URL)
            .centerCrop()
            .signature(ObjectKey(Constants.NO_PROFILE_PICTURE_URL))
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
            .into(this)

        binding.changePictureBtn.text = "Add Picture"
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}