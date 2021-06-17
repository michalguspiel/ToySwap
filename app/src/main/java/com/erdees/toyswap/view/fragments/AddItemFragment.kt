package com.erdees.toyswap.view.fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ClipData
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.*
import android.widget.GridLayout
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

    private var indexOfMovedElement: Int = 999 // changed during app run
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
            binding.itemAddPictureBtn.isEnabled = picList.size < 6
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


   private fun View.setMargins(topMargin : Int,bottomMargin : Int,leftMargin : Int, rightMargin : Int) {
       val layoutParams = GridLayout.LayoutParams()
       layoutParams.topMargin = topMargin
       layoutParams.bottomMargin = bottomMargin
       layoutParams.leftMargin = leftMargin
       layoutParams.rightMargin = rightMargin
       this.layoutParams = layoutParams
   }

    private fun PictureGridItemBinding.setUp(eachPic: Uri,picList: List<Uri>){
        if (eachPic == picList.first()) this.gridItemHead.text = "Main picture"
        Glide.with(requireActivity())
            .load(eachPic)
            .into(this.gridItemPicture)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun PictureGridItemBinding.setPictureTouchListener(eachPic : Uri){
        this.gridItemPicture.setOnTouchListener { v, event ->
            val action = event.action
            binding.root.requestDisallowInterceptTouchEvent(true)
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
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun PictureGridItemBinding.setLayoutTouchListener(picList: List<Uri>,eachPic: Uri){
        this.gridItemLayout.setOnTouchListener { view, event ->
            val indexOfElement = picList.indexOf(eachPic)
            indexOfMovedElement = indexOfElement
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
                else ->{
                    Log.i(TAG, " Motion ACTION  ELSE! ${action}")
                    return@setOnTouchListener true
                }
            }
        }
    }

    private fun PictureGridItemBinding.setLayoutDragListener(picList: List<Uri>,eachPic: Uri){
        val index = picList.indexOf(eachPic)
        this.gridItemLayout.setOnDragListener { v, event ->
            val action = event.action
            when (action) {
                DragEvent.ACTION_DRAG_ENDED -> {
                    Log.i(TAG,"LOC: x: ${event.x} , y: ${event.y} ")
                    Handler(Looper.getMainLooper()).post {
                        buildPictureGridLayout(picList)
                    }
                }
                DragEvent.ACTION_DROP -> {
                    viewModel.rearrangePictures(indexOfMovedElement, index)
                }
                else -> Log.i(TAG,"DRAGGIN ELSEEE!! $action")
            }
            return@setOnDragListener true
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun addEachPictureCard(picList: List<Uri>, eachPic : Uri) {
        val eachPictureCard =
            LayoutInflater.from(requireContext())
                .inflate(R.layout.picture_grid_item, null, false)
        val thisBinding = PictureGridItemBinding.bind(eachPictureCard)
        eachPictureCard.setMargins(12,12,16,16)
        thisBinding.setUp(eachPic,picList)
        thisBinding.setPictureTouchListener(eachPic)
        if(picList.size > 1) {
            thisBinding.setLayoutTouchListener(picList, eachPic)
            thisBinding.setLayoutDragListener(picList, eachPic)
        }
        thisBinding.gridRemovePicBtn.setOnClickListener {
            viewModel.removePicture(eachPic)
        }
        binding.addItemPicturesLayout.addView(eachPictureCard)
    }


    private fun buildPictureGridLayout(picList : List<Uri>) {
        binding.addItemPicturesLayout.removeAllViews()
        for (eachPic in picList) {
            addEachPictureCard(picList, eachPic)
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