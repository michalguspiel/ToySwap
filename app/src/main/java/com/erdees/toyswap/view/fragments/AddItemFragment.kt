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
import com.erdees.toyswap.viewModel.AddItemAndroidViewModel
import com.erdees.toyswap.viewModel.AddItemFragmentViewModel
import com.google.android.gms.tasks.Task
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference
import com.theartofdev.edmodo.cropper.CropImage


class AddItemFragment : Fragment() {


    private var _binding: FragmentAddItemBinding? = null
    private val binding get() = _binding!!

    private var indexOfMovedElement: Int = 0 // changed during app run TODO make it RX
    private lateinit var pickedCategory: ItemCategory

    private lateinit var picturePreview: PicturePreviewDialog

    private lateinit var viewModel: AddItemFragmentViewModel
    private lateinit var androidViewModel: AddItemAndroidViewModel

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
        androidViewModel = ViewModelProvider(this).get(AddItemAndroidViewModel::class.java)

        viewModel.categoryLiveData.observe(viewLifecycleOwner, {
            if (it != null) {
                Log.i(TAG, it.categoryName)
                pickedCategory = it
                binding.itemChosenCategory.text = it.categoryName
                binding.itemChooseCategoryBtn.text = getString(R.string.change_category)
            }
            if (it == null) {
                binding.itemChosenCategory.text = ""
                binding.itemChooseCategoryBtn.text = getString(R.string.pick_category)
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

        binding.submitItemButton.setOnClickListener {
            viewModel.addPicturesToCloud()
            // showLoadingDialog()
            viewModel.getUriOfPicsInCloudLiveData().observe(viewLifecycleOwner, { cloudUriList ->
                viewModel.picturesLiveData.observe(viewLifecycleOwner, { clientUriList ->
                    if (cloudUriList.size == clientUriList.size &&
                        cloudUriList.isNotEmpty() &&
                        clientUriList.isNotEmpty()
                    ) addThisItemToFirebase().addOnSuccessListener {
                        Log.i(TAG, "ITEM ADDED SUCCESSFULLY !!!")
                        viewModel.clearPicturesData()
                        //endLoadingDialogAndContinue()
                        // meybe show item page or Show all user items
                    }
                        .addOnFailureListener {
                            Log.i(TAG, "ITEM ADDING FAILED!")
                        }
                })
            })
            /**pseudocode
             *
             * upload pics to firebase storage, save their urls
             * showLoadingDialog
             *
             * once viewModel.cloudPicturesUriLive equals size of clientPicturesUri  [uploading finished]
             *
             * add item to firebase
             *once item added succesfully clear client Pics and clientCloudPicturesUriLive
             * Close loading dialog
             * show item page??? or stay in this fragment idk yet
             *
             *
             * */
        }


        return view
    }

    private fun addThisItemToFirebase(): Task<DocumentReference> {
        return viewModel.addItemToFirebase(
            binding.itemNameInput.text.toString(),
            pickedCategory,
            binding.itemDescInput.text.toString(),
            binding.itemPriceInput.text.toString().toDouble(),
            null,
            null,
            Timestamp.now(),
            androidViewModel.getUserId()!!
        )
    }

    /**FOR NOW THIS CANNOT BE CHANGED SINCE CROPPER LIBRARY DOESN'T WORK WITH [registerForActivityResult]*/
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


    private fun View.setMargins(
        topMargin: Int,
        bottomMargin: Int,
        leftMargin: Int,
        rightMargin: Int
    ) {
        val layoutParams = GridLayout.LayoutParams()
        layoutParams.topMargin = topMargin
        layoutParams.bottomMargin = bottomMargin
        layoutParams.leftMargin = leftMargin
        layoutParams.rightMargin = rightMargin
        this.layoutParams = layoutParams
    }

    private fun PictureGridItemBinding.setUp(eachPic: Uri, picList: List<Uri>) {
        if (eachPic == picList.first()) this.gridItemHead.text = getString(R.string.main_picture)
        Glide.with(requireActivity())
            .load(eachPic)
            .into(this.gridItemPicture)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun PictureGridItemBinding.setPictureTouchListenerAsPicPreview(eachPic: Uri) {
        this.gridItemPicture.setOnTouchListener { _, event ->
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
    private fun PictureGridItemBinding.setLayoutTouchListenerAsItemDrag(
        picList: List<Uri>,
        eachPic: Uri
    ) {
        this.gridItemLayout.setOnTouchListener { view, event ->
            indexOfMovedElement = picList.indexOf(eachPic)
            when (event.action) {
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
                else -> {
                    return@setOnTouchListener true
                }
            }
        }
    }

    private fun PictureGridItemBinding.setLayoutDragListener(picList: List<Uri>, eachPic: Uri) {
        val index = picList.indexOf(eachPic)
        this.gridItemLayout.setOnDragListener { _, event ->
            when (event.action) {
                DragEvent.ACTION_DRAG_ENDED -> {
                    Handler(Looper.getMainLooper()).post {
                        buildPictureGridLayout(picList)
                    }
                }
                DragEvent.ACTION_DROP -> {
                    viewModel.rearrangePictures(indexOfMovedElement, index)
                }
                else -> {
                }
            }
            return@setOnDragListener true
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun addEachPictureCard(picList: List<Uri>, eachPic: Uri) {
        val eachPictureCard =
            LayoutInflater.from(requireContext())
                .inflate(R.layout.picture_grid_item, null, false)
        val thisBinding = PictureGridItemBinding.bind(eachPictureCard)
        eachPictureCard.setMargins(12, 12, 16, 16)
        thisBinding.setUp(eachPic, picList)
        thisBinding.setPictureTouchListenerAsPicPreview(eachPic)
        if (picList.size > 1) { // SO THERE'S NO DRAGGING OPTION FOR ONE ITEM
            thisBinding.setLayoutTouchListenerAsItemDrag(picList, eachPic)
            thisBinding.setLayoutDragListener(picList, eachPic)
        }
        thisBinding.gridRemovePicBtn.setOnClickListener {
            viewModel.removePicture(eachPic)
        }
        binding.addItemPicturesLayout.addView(eachPictureCard)
    }


    private fun buildPictureGridLayout(picList: List<Uri>) {
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