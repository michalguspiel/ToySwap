package com.erdees.toyswap.fragments.dialogs

import android.graphics.Color.TRANSPARENT
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.erdees.toyswap.databinding.DialogChangeAddressBinding
import com.google.firebase.auth.FirebaseAuth

class ChangeAddressDialog : DialogFragment() {

    private var _binding : DialogChangeAddressBinding? = null
            val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DialogChangeAddressBinding.inflate(inflater,container,false)
        val view = binding.root


        dialog?.window?.setBackgroundDrawable(ColorDrawable(TRANSPARENT))
        return view
    }


    companion object {
        const val TAG = "ChangeAddressDialog"
        fun newInstance() : ChangeAddressDialog = ChangeAddressDialog()
    }
}