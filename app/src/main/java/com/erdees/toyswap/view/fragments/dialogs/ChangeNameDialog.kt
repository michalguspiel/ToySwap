package com.erdees.toyswap.view.fragments.dialogs

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.erdees.toyswap.model.utils.Utils.endLoading
import com.erdees.toyswap.model.utils.Utils.showLoading
import com.erdees.toyswap.databinding.DialogChangeNameBinding
import com.erdees.toyswap.viewModel.ChangeNameDialogViewModel

class ChangeNameDialog : DialogFragment() {

    private var _binding: DialogChangeNameBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: ChangeNameDialogViewModel

    private val progressBar by lazy{
        ProgressBar(requireContext())
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = DialogChangeNameBinding.inflate(inflater, container, false)
        val view = binding.root
        viewModel = ViewModelProvider(this).get(ChangeNameDialogViewModel::class.java)

        viewModel.clientUserLiveData.observe(viewLifecycleOwner, {
            if (it != null) setFields(it.firstName,it.lastName)
        })

        binding.nameSubmitBtn.setOnClickListener {
            view.showLoading(progressBar)
            if(bothNamesAreProvided()) viewModel.updateNames(binding.firstName.text.toString(),binding.lastName.text.toString())
                ?.addOnSuccessListener { view.endLoading(progressBar)
                this.dismiss()}
        }

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        return view

    }

    private fun bothNamesAreProvided() : Boolean{
        return (!binding.firstName.text.isNullOrBlank() && !binding.lastName.text.isNullOrBlank())
    }

    private fun setFields(firstName : String, lastName: String){
        binding.firstName.setText(firstName)
        binding.lastName.setText(lastName)
    }

    companion object {
        const val TAG = "ChangeNameDialog"
    }

}