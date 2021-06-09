package com.erdees.toyswap.view.fragments.dialogs

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.erdees.toyswap.databinding.PicturePreviewDialogBinding

class PicturePreviewDialog(private val picUri: Uri) : DialogFragment() {

    private var _binding: PicturePreviewDialogBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = PicturePreviewDialogBinding.inflate(inflater, container, false)
        val view = binding.root


        Glide.with(requireContext())
            .load(picUri)
            .centerCrop()
            .into(binding.imagePreview)


        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return view
    }

    companion object {
        fun newInstance(picUri: Uri): PicturePreviewDialog = PicturePreviewDialog(picUri)
        const val TAG = "PicturePreviewDialog"
    }



}