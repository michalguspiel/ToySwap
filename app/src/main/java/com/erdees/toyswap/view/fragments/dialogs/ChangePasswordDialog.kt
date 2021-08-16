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
import com.erdees.toyswap.model.utils.Utils.endLoading
import com.erdees.toyswap.model.utils.Utils.makeToast
import com.erdees.toyswap.model.utils.Utils.showLoading
import com.erdees.toyswap.databinding.DialogChangePasswordBinding
import com.erdees.toyswap.model.utils.Registration
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
            view.showLoading(progressBar)
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
                    view.endLoading(progressBar)
                }
                    ?.addOnFailureListener {
                        passwordChangeError(registration.errorMessage)
                    }
            }
        }

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return view
    }


    companion object {
        const val TAG = "ChangePasswordDialog"
    }

    private fun passwordChangeError(message: String){
        listener.passwordChangedSuccessfully = false
        view.makeToast(message)
        view.endLoading(progressBar)
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