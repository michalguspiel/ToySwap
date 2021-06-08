package com.erdees.toyswap.view.fragments.dialogs

import android.annotation.SuppressLint
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

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = PicturePreviewDialogBinding.inflate(inflater, container, false)
        val view = binding.root


        Glide.with(requireContext())
            .load(picUri)
            .into(binding.imagePreview)

        return view
    }

    companion object {
        fun newInstance(picUri: Uri): PicturePreviewDialog = PicturePreviewDialog(picUri)
        const val TAG = "PicturePreviewDialog"
    }



}