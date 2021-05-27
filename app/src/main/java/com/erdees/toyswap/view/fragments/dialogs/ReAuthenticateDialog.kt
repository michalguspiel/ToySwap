package com.erdees.toyswap.view.fragments.dialogs

import android.graphics.Color
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
import com.erdees.toyswap.databinding.DialogReAuthenticateBinding
import com.erdees.toyswap.viewModel.ReAuthViewModel
import com.google.firebase.auth.EmailAuthProvider

class ReAuthenticateDialog(override var passwordChangedSuccessfully: Boolean?) : DialogFragment(),MyAccountDialogsListener {

    private var _binding: DialogReAuthenticateBinding? = null
    private val binding get() = _binding!!
    private lateinit var view : LinearLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogReAuthenticateBinding.inflate(inflater,container,false)

        view = binding.root

        val viewModel = ViewModelProvider(this).get(ReAuthViewModel::class.java)

        binding.reAuthenticateBtn.setOnClickListener {
            val cred = EmailAuthProvider.getCredential(binding.emailInput.text.toString(),binding.passwordInput.text.toString())
            viewModel.reAuthenticate(cred)?.addOnCompleteListener{ task ->
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
        view.makeToast("Authentication failed! Wrong email or password.")
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}