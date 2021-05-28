package com.erdees.toyswap.view.fragments

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
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
import com.erdees.toyswap.Utils.endLoading
import com.erdees.toyswap.Utils.makeGone
import com.erdees.toyswap.Utils.makeVisible
import com.erdees.toyswap.Utils.showLoading
import com.erdees.toyswap.databinding.FragmentMyAccountBinding
import com.erdees.toyswap.model.firebase.ClientUser
import com.erdees.toyswap.view.activities.MainActivity
import com.erdees.toyswap.view.fragments.dialogs.ChangeAddressDialog
import com.erdees.toyswap.view.fragments.dialogs.ReAuthenticateDialog
import com.erdees.toyswap.viewModel.MyAccountFragmentViewModel
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.storage.FirebaseStorage
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlin.random.Random


class MyAccountFragment : Fragment() {

    private var _binding: FragmentMyAccountBinding? = null
    private val binding get() = _binding!!

    private lateinit var user: FirebaseUser
    private lateinit var thisClientUser: ClientUser

    private lateinit var viewModel: MyAccountFragmentViewModel
    private lateinit var view: ScrollView

    private lateinit var generatedId: String

    private val storage = FirebaseStorage.getInstance()
    private val storageRef = storage.reference
    private fun profilePicImageRef() = storageRef.child("profilePics/${user.uid}_profile_pic.jpg")
    private val progressBar by lazy {
        ProgressBar(requireContext())
    }

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
                thisClientUser = it
            }
        })

        viewModel.addressLiveData.observe(viewLifecycleOwner, { address ->
            with(address) {
                if (street.isEmpty() && postCode.isEmpty() && city.isEmpty()) setEmptyFields()
                else setFields(street, postCode, city)
            }
        })

        viewModel.isUserLoggedOutLiveData.observe(viewLifecycleOwner, {
            if (it) restartActivity()
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

        binding.changePictureBtn.setOnClickListener {
            openGalleryForImage()
        }

        return view
    }

    companion object {
        fun newInstance(): MyAccountFragment = MyAccountFragment()
        const val TAG = "MyAccountFragment"
        const val REQUEST_CODE = 149
    }

    private fun openDialogToReAuthenticate() {
        val reAuthDialog = ReAuthenticateDialog(false)
        reAuthDialog.show(childFragmentManager, ReAuthenticateDialog.TAG)
    }

    private fun restartActivity() {
        val mainActivity = Intent(context, MainActivity::class.java)
        startActivity(mainActivity)
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
                if (it.name.isNotBlank()) binding.accountFullNameTV.text = it.name
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


    private fun launchImageCrop(uri: Uri) {
        CropImage.activity(uri)
            .setGuidelines(CropImageView.Guidelines.ON)
            .setAspectRatio(400, 400)
            .setCropShape(CropImageView.CropShape.RECTANGLE)
            .start(context!!, this)
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
                launchImageCrop(uri)
            }
        } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == RESULT_OK) {
                val resultUri = result.uri
                uploadImage(resultUri)
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                result.error
            }

        }
    }

    private fun uploadImage(imageUri: Uri) {
        binding.profilePicLayout.showLoading(progressBar)
        randomizeNumber()
        profilePicImageRef().delete().addOnCompleteListener {
            profilePicImageRef().putFile(imageUri)
                .addOnSuccessListener {
                profilePicImageRef().downloadUrl.addOnSuccessListener { uri ->
                    Log.i("TEST GOT IT? ", uri.toString())
                    viewModel.updateAvatar(uri.toString())
                        ?.addOnSuccessListener { binding.profilePicLayout.endLoading(progressBar) }
                }
                    .addOnFailureListener {
                        Log.i("TEST", "fail")
                    }
            }
        }
    }

    private fun openGalleryForImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_CODE)
    }

    private fun randomizeNumber() {
        val randomSeed = Random(System.currentTimeMillis())
        val randomNumber = randomSeed.nextLong(9999999L, 999999999L)
        generatedId = randomNumber.toString()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}