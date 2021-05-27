package com.erdees.toyswap.view.fragments.dialogs

import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.erdees.toyswap.Utils.makeToast
import com.erdees.toyswap.databinding.DialogChangePasswordBinding
import com.erdees.toyswap.model.Registration
import com.erdees.toyswap.viewModel.ChangePasswordDialogViewModel
import com.google.firebase.auth.FirebaseUser

class ChangePasswordDialog(private val listener: MyAccountDialogsListener) : DialogFragment() {

    private var _binding: DialogChangePasswordBinding? = null
    private val binding get() = _binding!!


    private lateinit var user: FirebaseUser
    private lateinit var viewModel: ChangePasswordDialogViewModel
    private lateinit var view : LinearLayout
    private val progressBar by lazy{
        ProgressBar(requireContext())
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogChangePasswordBinding.inflate(inflater, container, false)
        view = binding.root
        viewModel = ViewModelProvider(this).get(ChangePasswordDialogViewModel::class.java)

        viewModel.userLiveData.observe(viewLifecycleOwner,{
            if (it != null) {
                user = it
            }
        })

        binding.changePasswordSubmitBtn.setOnClickListener {
            showLoading()
            val registration = Registration(
                binding.passwordInput1.text.toString(),
                binding.passwordInput2.text.toString(),
                user.email.toString()
            )
            if (!registration.isLegit()) {
                passwordChangeError(registration.errorMessage)
            }
            else {
                viewModel.changePassword(registration)?.addOnSuccessListener {
                    listener.passwordChangedSuccessfully = true
                    this.dismiss()
                    endLoading()
                }
                    ?.addOnFailureListener {
                        passwordChangeError(registration.errorMessage)
                    }
            }
        }

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return view
    }

    private fun showLoading(){
        view.addView(progressBar)
    }

    private fun endLoading(){
        view.removeView(progressBar)
    }

    companion object {
        const val TAG = "ChangePasswordDialog"
    }

    private fun passwordChangeError(message: String){
        listener.passwordChangedSuccessfully = false
        view.makeToast(message)
        endLoading()
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