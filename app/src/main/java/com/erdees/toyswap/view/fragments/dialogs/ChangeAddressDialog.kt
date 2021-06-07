package com.erdees.toyswap.view.fragments.dialogs

import android.graphics.Color.TRANSPARENT
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.erdees.toyswap.Utils.makeSnackbar
import com.erdees.toyswap.Utils.makeToast
import com.erdees.toyswap.databinding.DialogChangeAddressBinding
import com.erdees.toyswap.model.models.Address
import com.erdees.toyswap.viewModel.ChangeAddressDialogViewModel
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class ChangeAddressDialog : DialogFragment() {

    private var _binding: DialogChangeAddressBinding? = null
    private val binding get() = _binding!!

    private lateinit var db: FirebaseFirestore
    private  var user: FirebaseUser? = null
    private lateinit var userDocRef: DocumentReference
    private lateinit var view: LinearLayout
    private lateinit var viewModel: ChangeAddressDialogViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogChangeAddressBinding.inflate(inflater, container, false)
        view = binding.root
        db = Firebase.firestore
        viewModel = ViewModelProvider(this).get(ChangeAddressDialogViewModel::class.java)
        viewModel.userLiveData.observe(viewLifecycleOwner,{firebaseUser ->
            user = firebaseUser
            if(firebaseUser != null) userDocRef = db.collection("users").document(user!!.uid)
        })

        viewModel.clientUserLiveData.observe(viewLifecycleOwner,{
            if (it != null) {
                setFields(it.addressStreet,it.addressPostCode,it.addressCity)
            }
        })


        binding.addressSubmitBtn.setOnClickListener {
            if (atLeastOneInputIsEmpty()) view.makeToast("Fill every field.")
            else {
                val newAddress = Address(
                    binding.addressStreet.text.toString(),
                    binding.addressPostCode.text.toString(),
                    binding.addressCity.text.toString()
                )
                viewModel.updateAddress(newAddress)?.addOnSuccessListener {
                    this.dismiss()
                    parentFragment?.view?.makeSnackbar("Address updated!")
                }
                    ?.addOnFailureListener {
                        this.dismiss()
                        parentFragment?.view?.makeSnackbar("Something went wrong!")
                    }
            }
        }

        dialog?.window?.setBackgroundDrawable(ColorDrawable(TRANSPARENT))
        return view
    }


    private fun atLeastOneInputIsEmpty(): Boolean {
        return (binding.addressCity.text.isNullOrBlank() ||
                binding.addressPostCode.text.isNullOrBlank() ||
                binding.addressStreet.text.isNullOrBlank())
    }

    private fun setFields(street: String, postCode: String, city: String) {
        if (street.isEmpty()) binding.addressStreet.setText("") else binding.addressStreet.setText(
            street
        )
        if (postCode.isEmpty()) binding.addressPostCode.setText("") else binding.addressPostCode.setText(
            postCode
        )
        if (city.isEmpty()) binding.addressCity.setText("") else binding.addressCity.setText(city)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TAG = "ChangeAddressDialog"
    }
}