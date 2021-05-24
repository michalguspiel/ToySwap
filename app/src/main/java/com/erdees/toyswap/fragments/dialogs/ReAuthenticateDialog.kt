package com.erdees.toyswap.fragments.dialogs

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.DialogFragment
import com.erdees.toyswap.Utils.makeSnackbar
import com.erdees.toyswap.Utils.makeToast
import com.erdees.toyswap.databinding.DialogReAuthenticateBinding
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ReAuthenticateDialog(override var passwordChangedSuccessfully: Boolean?) : DialogFragment(),MyAccountDialogsListener {
    private var _binding: DialogReAuthenticateBinding? = null
    private val binding get() = _binding!!

    private lateinit var view : LinearLayout
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var user: FirebaseUser

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogReAuthenticateBinding.inflate(inflater,container,false)

        view = binding.root
        auth = Firebase.auth
        db = Firebase.firestore
        user = auth.currentUser!! // not nullable cause This dialog can't be opened unless somebody is logged in.


        binding.reAuthenticateBtn.setOnClickListener {
            val cred = EmailAuthProvider.getCredential(binding.emailInput.text.toString(),binding.passwordInput.text.toString())
            user.reauthenticate(cred).addOnCompleteListener { task ->
                if(task.isSuccessful) continueWithChangingPassword()
                    else authenticationFailed()
            }
        }

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        return view
    }

    private fun continueWithChangingPassword(){
    val changePasswordDialog = ChangePasswordDialog(this)
        changePasswordDialog.show(childFragmentManager,ChangePasswordDialog.TAG)
    }

    private fun authenticationFailed(){
        view.makeToast("Authentication failed!")
        binding.emailInput.text?.clear()
        binding.passwordInput.text?.clear()
    }

    companion object {
        const val TAG = "ReAuthenticateDialog"
    }

    override fun displaySnackBar(){
        parentFragment?.view?.makeSnackbar("Password changed!")
    }

    override fun onCloseDialog() {
        this.dismiss()
    }

}