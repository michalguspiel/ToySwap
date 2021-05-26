package com.erdees.toyswap.view.fragments.dialogs

import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.erdees.toyswap.Utils.makeToast
import com.erdees.toyswap.model.Registration
import com.erdees.toyswap.databinding.DialogChangePasswordBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ChangePasswordDialog(private val listener: MyAccountDialogsListener) : DialogFragment() {

    private var _binding: DialogChangePasswordBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var user: FirebaseUser

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogChangePasswordBinding.inflate(inflater, container, false)
        val view = binding.root

        auth = Firebase.auth
        db = Firebase.firestore
        user = auth.currentUser!! // not nullable cause This dialog can't be opened unless somebody is logged in.


        binding.changePasswordSubmitBtn.setOnClickListener {
            val registration = Registration(
                binding.passwordInput1.text.toString(),
                binding.passwordInput2.text.toString(),
                user.email.toString()
            )
            if (!registration.isLegit()) {
                listener.passwordChangedSuccessfully = false
                view.makeToast(registration.errorMessage)

            } else {
                user.updatePassword(binding.passwordInput1.text.toString())
                listener.passwordChangedSuccessfully = true
                this.dismiss()
            }
        }

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return view
    }

    companion object {
        const val TAG = "ChangePasswordDialog"
    }

    override fun onDismiss(dialog: DialogInterface) {
        if (listener.passwordChangedSuccessfully == true) listener.displaySnackBar()
        listener.onCloseDialog()
        super.onDismiss(dialog)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}