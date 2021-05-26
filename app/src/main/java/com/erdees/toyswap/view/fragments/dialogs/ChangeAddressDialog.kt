package com.erdees.toyswap.view.fragments.dialogs

import android.content.DialogInterface
import android.graphics.Color.TRANSPARENT
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.DialogFragment
import com.erdees.toyswap.Utils.makeSnackbar
import com.erdees.toyswap.Utils.makeToast
import com.erdees.toyswap.databinding.DialogChangeAddressBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ChangeAddressDialog(private val listener: MyAccountDialogsListener) : DialogFragment() {

    private var _binding : DialogChangeAddressBinding? = null
            private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth
    private lateinit var db : FirebaseFirestore
    private lateinit var user : FirebaseUser
    private lateinit var userDocRef : DocumentReference
    private lateinit var view : LinearLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogChangeAddressBinding.inflate(inflater,container,false)
        view = binding.root

        auth = Firebase.auth
        db = Firebase.firestore
        user = auth.currentUser!! // not nullable cause This dialog can't be opened unless somebody is logged in.

        userDocRef = db.collection("users").document(user.uid)

            userDocRef.get().addOnSuccessListener {
                val street = (it["addressStreet"].toString())
                val postCode = (it["addressPostCode"].toString())
                val city = (it["addressCity"].toString())
                setFields(street,postCode,city)
            }

        binding.addressSubmitBtn.setOnClickListener {
            updateAddress()
        }

        dialog?.window?.setBackgroundDrawable(ColorDrawable(TRANSPARENT))
        return view
    }

    private fun updateAddress(){
        if (atLeastOneInputIsEmpty()) view.makeToast("Fill every field.")
        else {
            userDocRef.update(
                "addressStreet", binding.addressStreet.text.toString(),
                "addressPostCode", binding.addressPostCode.text.toString(),
                "addressCity", binding.addressCity.text.toString()
            )
            this.dismiss()
            parentFragment?.view?.makeSnackbar("Address updated!")
        }
    }

    private fun atLeastOneInputIsEmpty():Boolean{
        return (binding.addressCity.text.isNullOrBlank() ||
                binding.addressPostCode.text.isNullOrBlank() ||
                binding.addressStreet.text.isNullOrBlank())
    }

    private fun setFields(street: String, postCode : String, city :String){
        if(street.isEmpty()) binding.addressStreet.setText("") else binding.addressStreet.setText(street)
        if(postCode.isEmpty()) binding.addressPostCode.setText("") else binding.addressPostCode.setText(postCode)
        if(city.isEmpty()) binding.addressCity.setText("") else binding.addressCity.setText(city)
    }

    override fun onDismiss(dialog: DialogInterface) {
        listener.onCloseDialog()
        super.onDismiss(dialog)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TAG = "ChangeAddressDialog"
    }
}