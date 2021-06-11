package com.erdees.toyswap.view.fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ClipData
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.erdees.toyswap.Constants
import com.erdees.toyswap.R
import com.erdees.toyswap.Utils
import com.erdees.toyswap.Utils.makeGone
import com.erdees.toyswap.databinding.FragmentAddItemBinding
import com.erdees.toyswap.databinding.PictureGridItemBinding
import com.erdees.toyswap.model.models.item.ItemCategory
import com.erdees.toyswap.view.fragments.dialogs.ChooseCategoryDialog
import com.erdees.toyswap.view.fragments.dialogs.PicturePreviewDialog
import com.erdees.toyswap.viewModel.AddItemFragmentViewModel
import com.theartofdev.edmodo.cropper.CropImage

class AddItemFragment : Fragment() {


    private var _binding: FragmentAddItemBinding? = null
    private val binding get() = _binding!!

    private lateinit var pickedCategory: ItemCategory

    private lateinit var picturePreview: PicturePreviewDialog

    private lateinit var viewModel: AddItemFragmentViewModel

    private val chooseCategoryDialog by lazy {
        ChooseCategoryDialog.newInstance()
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddItemBinding.inflate(inflater, container, false)

        val view = binding.root
        viewModel = ViewModelProvider(this).get(AddItemFragmentViewModel::class.java)


        viewModel.categoryLiveData.observe(viewLifecycleOwner, {
            if (it != null) {
                Log.i(TAG, it.categoryName)
                pickedCategory = it
                binding.itemChosenCategory.text = it.categoryName
                binding.itemChooseCategoryBtn.text = "Change category"
            }
            if (it == null) {
                binding.itemChosenCategory.text = ""
                binding.itemChooseCategoryBtn.text = "Pick category"
            }
        })

        viewModel.picturesLiveData.observe(viewLifecycleOwner, { picList ->
            buildPictureGridLayout(picList)
        })

        binding.itemChooseCategoryBtn.setOnClickListener {
            chooseCategoryDialog.show(childFragmentManager, ChooseCategoryDialog.TAG)
        }

        binding.itemAddPictureBtn.setOnClickListener {
            openGalleryForImage()
        }

        return view
    }

    private fun openGalleryForImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, Constants.REQUEST_CODE)
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(
            requestCode,
            resultCode,
            data
        )
        if (requestCode == Constants.REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            data.data?.let { uri ->
                Utils.launchImageCrop(uri, requireContext(), this, 450, 600)
            }
        } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == Activity.RESULT_OK) {
                val resultUri = result.uri
                viewModel.addPicture(resultUri)
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                result.error
            }
        }
    }


    @SuppressLint("ClickableViewAccessibility")
    fun buildPictureGridLayout(picList : List<Uri>){
        binding.addItemPicturesLayout.removeAllViews()
        for (eachPic in picList) {
            val eachPictureCard =
                LayoutInflater.from(requireContext())
                    .inflate(R.layout.picture_grid_item, null, false)
            val thisBinding = PictureGridItemBinding.bind(eachPictureCard)
            if (eachPic == picList.first()) thisBinding.gridItemHead.text = "Main picture"
            Glide.with(requireActivity())
                .load(eachPic)
                .into(thisBinding.gridItemPicture)

            thisBinding.gridItemPicture.setOnTouchListener { v, event ->
                val action = event.action
                binding.root.requestDisallowInterceptTouchEvent(true)
                Log.i(TAG, action.toString())
                when (action) {
                    MotionEvent.ACTION_DOWN -> {
                        binding.root.disableScrollView()
                        picturePreview = PicturePreviewDialog.newInstance(eachPic)
                        picturePreview.show(parentFragmentManager, TAG)
                    }
                    MotionEvent.ACTION_UP -> {
                        binding.root.enableScrollView()
                        picturePreview.dismiss()

                    }
                }
                return@setOnTouchListener true
            }

            thisBinding.gridItemLayout.setOnTouchListener { view, event ->
                val action = event.action
                when (action) {
                    MotionEvent.ACTION_DOWN -> {
                        val data = ClipData.newPlainText("", "")
                        val shadowBuilder = View.DragShadowBuilder(view)
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            view.startDragAndDrop(data, shadowBuilder, view, 0)
                        } else {
                            view.startDrag(data, shadowBuilder, view, 0)
                        }
                        view.makeGone()
                        return@setOnTouchListener true
                    }
                    MotionEvent.ACTION_UP -> {
                        buildPictureGridLayout(picList)
                        return@setOnTouchListener true
                    }
                    else ->{
                        buildPictureGridLayout(picList)
                        return@setOnTouchListener false
                    }
                }
            }

            thisBinding.gridItemLayout.setOnDragListener { v, event ->
                val action = event.action
                when (action) {
                    DragEvent.ACTION_DRAG_STARTED -> {
                        Log.i(TAG, "START DRAGGIN")
                    }
                    DragEvent.ACTION_DRAG_ENDED -> {
                        buildPictureGridLayout(picList)
                        Log.i(TAG, "END DRAGGIN")
                    }
                    DragEvent.ACTION_DROP -> {
                        buildPictureGridLayout(picList)
                    }
                }

                return@setOnDragListener true
            }

            thisBinding.gridRemovePicBtn.setOnClickListener {
                viewModel.removePicture(eachPic)
            }
            binding.addItemPicturesLayout.addView(eachPictureCard)
        }
    }

    companion object {
        fun newInstance(): AddItemFragment = AddItemFragment()
        const val TAG = "AddItemFragment"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}