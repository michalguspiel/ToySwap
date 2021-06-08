package com.erdees.toyswap.view.fragments

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.ScrollView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.erdees.toyswap.Constants
import com.erdees.toyswap.Constants.REQUEST_CODE
import com.erdees.toyswap.Utils.launchImageCrop
import com.erdees.toyswap.Utils.makeGone
import com.erdees.toyswap.Utils.makeVisible
import com.erdees.toyswap.databinding.FragmentMyAccountBinding
import com.erdees.toyswap.model.models.ClientUser
import com.erdees.toyswap.view.activities.MainActivity
import com.erdees.toyswap.view.fragments.dialogs.ChangeAddressDialog
import com.erdees.toyswap.view.fragments.dialogs.ChangeNameDialog
import com.erdees.toyswap.view.fragments.dialogs.ReAuthenticateDialog
import com.erdees.toyswap.viewModel.MyAccountFragmentViewModel
import com.google.firebase.auth.FirebaseUser
import com.theartofdev.edmodo.cropper.CropImage


class MyAccountFragment : Fragment() {

    private var _binding: FragmentMyAccountBinding? = null
    private val binding get() = _binding!!

    private lateinit var user: FirebaseUser
    private lateinit var thisClientUser: ClientUser

    private lateinit var viewModel: MyAccountFragmentViewModel
    private lateinit var view: ScrollView

    private lateinit var alertDialog: AlertDialog



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentMyAccountBinding.inflate(inflater, container, false)
        view = binding.root
        viewModel = ViewModelProvider(this).get(MyAccountFragmentViewModel::class.java)


        viewModel.userLiveData.observe(viewLifecycleOwner, {
            if (it != null) {
                user = it
                setUserNameAndMail()
                binding.profilePic.setAvatar()
                setButtonsAccordingly()
            }
        })

        viewModel.clientUserData.observe(viewLifecycleOwner, {
            if (it != null) {
                with(it) {
                    if (addressStreet.isEmpty() && addressPostCode.isEmpty() && addressCity.isEmpty()) setEmptyFields()
                    else setFields(addressStreet, addressPostCode, addressCity)

                    setName("${it.firstName} ${it.lastName}")
                }
            }
        })


        viewModel.isUserLoggedOutLiveData.observe(viewLifecycleOwner, {
            if (it) openMainActivity()
        })


        binding.changeAddressBtn.setOnClickListener {
            val changeAddressDialog = ChangeAddressDialog()
            changeAddressDialog.show(childFragmentManager, ChangeAddressDialog.TAG)
        }

        binding.logoutBtn.setOnClickListener {
            viewModel.signOut()
            openMainActivity()
        }

        binding.changePasswordBtn.setOnClickListener {
            openDialogToReAuthenticate()
        }

        binding.changePictureBtn.setOnClickListener {
            openGalleryForImage()
        }

        binding.changeNameBtn.setOnClickListener {
            val changeNameDialog = ChangeNameDialog()
            changeNameDialog.show(childFragmentManager,ChangeNameDialog.TAG)
        }

        return view
    }

    companion object {
        fun newInstance(): MyAccountFragment = MyAccountFragment()
        const val TAG = "MyAccountFragment"
    }

    private fun openDialogToReAuthenticate() {
        val reAuthDialog = ReAuthenticateDialog(false)
        reAuthDialog.show(childFragmentManager, ReAuthenticateDialog.TAG)
    }

    private fun openMainActivity() {
        val mainActivity = Intent(context, MainActivity::class.java)
        startActivity(mainActivity)
    }

    private fun setName(name: String){
        if(name.isBlank()) binding.changeNameBtn.text = "Provide name"
        else {
            binding.accountFullNameTV.text = name
            binding.changeNameBtn.text = "Change name"
        }

    }

    private fun setButtonsAccordingly() {
        viewModel.userAuthProviderLiveData.observe(viewLifecycleOwner, {
            if (it == "google.com") binding.changePasswordBtn.makeGone()
            else binding.changePasswordBtn.makeVisible()
        })
    }

    private fun setUserNameAndMail() {
        viewModel.clientUserData.observe(viewLifecycleOwner, {
            if (it != null) {
                val name = it.firstName + " " + it.lastName
                if (name.isNotBlank()) binding.accountFullNameTV.text = name
                else binding.accountFullNameTV.text = ""
                binding.accountEmailTV.text = it.emailAddress
            }
        })
    }

    @SuppressLint("SetTextI18n")
    private fun ImageView.setAvatar() {
        viewModel.clientUserData.observe(viewLifecycleOwner, {
            val thisUserProfilePictureUrl = it?.avatar
            if (thisUserProfilePictureUrl != null && thisUserProfilePictureUrl.isNotBlank() && thisUserProfilePictureUrl != "null") {
                Glide.with(requireActivity())
                    .load(thisUserProfilePictureUrl)
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .into(this)

                binding.changePictureBtn.text = "Change Picture"
            } else this.setAnonProfilePicture()
        })
    }

    @SuppressLint("SetTextI18n")
    private fun setEmptyFields() {
        binding.addressTV.text = "You have not provided address."
        binding.addressPostCodeTV.makeGone()
        binding.addressCityTV.makeGone()
    }

    private fun setFields(street: String, postCode: String, city: String) {
        binding.addressTV.text = street
        binding.addressPostCodeTV.text = postCode
        binding.addressCityTV.text = city
        binding.addressPostCodeTV.makeVisible()
        binding.addressCityTV.makeVisible()
    }

    @SuppressLint("SetTextI18n")
    private fun ImageView.setAnonProfilePicture() {
        Glide.with(requireActivity())
            .load(Constants.NO_PROFILE_PICTURE_URL)
            .centerCrop()
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
            .into(this)

        binding.changePictureBtn.text = "Add Picture"
    }


    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(
            requestCode,
            resultCode,
            data
        )
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null && data.data != null) {
            data.data?.let { uri ->
                launchImageCrop(uri,requireContext(),this,400,400)
            }
        } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == RESULT_OK) {
                val resultUri = result.uri
                changeAvatar(resultUri)
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                result.error
            }

        }
    }

    private fun changeAvatar(imageUri: Uri){
        showLoading()
        viewModel.deleteCurrentAvatar().addOnCompleteListener {
            viewModel.uploadNewAvatar(imageUri).addOnSuccessListener {
                viewModel.getNewAvatarUrl().addOnSuccessListener {  newAvatarUrl ->
                    viewModel.updateAvatar(newAvatarUrl.toString())?.addOnSuccessListener { endLoading() }
                }
            }
        }
    }

    private fun showLoading(){
         alertDialog = AlertDialog.Builder(requireContext())
             .setView(ProgressBar(requireContext()))
             .setMessage("Loading...")
             .show()

    }

    private fun endLoading(){
        alertDialog.dismiss()
    }

    private fun openGalleryForImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_CODE)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}