package com.erdees.toyswap.view.fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.ClipData
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.*
import android.widget.ProgressBar
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.erdees.toyswap.R
import com.erdees.toyswap.Utils
import com.erdees.toyswap.Utils.disable
import com.erdees.toyswap.Utils.enable
import com.erdees.toyswap.Utils.makeGone
import com.erdees.toyswap.Utils.postRxValueBasedOnEditText
import com.erdees.toyswap.Utils.setMargins
import com.erdees.toyswap.databinding.FragmentAddItemBinding
import com.erdees.toyswap.databinding.PictureGridItemBinding
import com.erdees.toyswap.model.Constants
import com.erdees.toyswap.view.fragments.dialogs.ChooseCategoryDialog
import com.erdees.toyswap.view.fragments.dialogs.PicturePreviewDialog
import com.erdees.toyswap.viewModel.AddItemFragmentViewModel
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.theartofdev.edmodo.cropper.CropImage
import io.reactivex.rxjava3.subjects.PublishSubject


class AddItemFragment : Fragment() {


    private var _binding: FragmentAddItemBinding? = null
    private val binding get() = _binding!!

    private var indexOfMovedElement: Int = 0

    private lateinit var picturePreview: PicturePreviewDialog

    private lateinit var viewModel: AddItemFragmentViewModel

    private lateinit var alertDialog: AlertDialog

     val chooseCategoryDialog by lazy {
        ChooseCategoryDialog.newInstance()
    }

    private val isPictureProvidedRX = PublishSubject.create<Boolean>()
    private val isCategoryProvidedRX = PublishSubject.create<Boolean>()
    private val isNameProvidedRX = PublishSubject.create<Boolean>()
    private val isDescProvidedRX = PublishSubject.create<Boolean>()
    private val isPriceProvidedRX = PublishSubject.create<Boolean>()

    private var isPictureProvided = false
    private var isCategoryProvided = false
    private var isNameProvided = false
    private var isDescProvided = false
    private var isPriceProvided = false

    override fun onResume() {
        Log.i(TAG,"on resume")
        super.onResume()
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddItemBinding.inflate(inflater, container, false)
        val view = binding.root
        viewModel = ViewModelProvider(this).get(AddItemFragmentViewModel::class.java)

        ifAllDataIsProvidedEnableSubmitButton()

        viewModel.categoryLiveData.observe(viewLifecycleOwner, {
            if (it != null) {
                Log.i(TAG, "Picked category  = ${it.categoryName}")
                binding.itemChosenCategory.text = it.categoryName
                binding.itemChooseCategoryBtn.text = getString(R.string.change_category)
                isCategoryProvidedRX.onNext(true)
            }
            if (it == null) {
                isCategoryProvidedRX.onNext(false)
                binding.itemChosenCategory.text = ""
                binding.itemChooseCategoryBtn.text = getString(R.string.pick_category)
            }
        })

        viewModel.picturesLiveData.observe(viewLifecycleOwner, { picList ->
            if (picList.isNotEmpty()) isPictureProvidedRX.onNext(true) else isPictureProvidedRX.onNext(
                false
            )
            binding.itemAddPictureBtn.isEnabled = picList.size < 6
            buildPictureGridLayout(picList)
        })

        binding.itemChooseCategoryBtn.setOnClickListener {
            chooseCategoryDialog.show(parentFragmentManager, ChooseCategoryDialog.TAG)
        }

        binding.itemAddPictureBtn.setOnClickListener {
            openGalleryForImage()
        }

        binding.itemNameInput.addTextChangedListener {
            if (it != null) isNameProvidedRX.postRxValueBasedOnEditText(it)
        }
        binding.itemDescInput.addTextChangedListener {
            if (it != null) isDescProvidedRX.postRxValueBasedOnEditText(it)
        }
        binding.itemPriceInput.addTextChangedListener {
            if (it != null) isPriceProvidedRX.postRxValueBasedOnEditText(it)
        }


        binding.submitItemButton.setOnClickListener {
            viewModel.addPicturesToCloud()
            showLoadingDialog()
            viewModel.getUriOfPicsInCloudLiveData().observe(viewLifecycleOwner, { cloudUriList ->
                viewModel.picturesLiveData.observe(viewLifecycleOwner, { clientUriList ->
                    if (cloudUriList.size == clientUriList.size &&
                        cloudUriList.isNotEmpty() &&
                        clientUriList.isNotEmpty()
                    ) addThisItemToFirebase().addOnSuccessListener {
                        Log.i(TAG, "Item added succesfully!")
                        viewModel.clearPicturesData()
                        endLoadingAndContinue()
                    }
                        .addOnFailureListener {
                            Log.i(TAG, "Item adding failed, handle error here.")
                            endLoadingAndShowFailureDialog()
                        }
                })
            })
        }

        isPictureProvidedRX.subscribe {
            isPictureProvided = it
            ifAllDataIsProvidedEnableSubmitButton()
        }
        isCategoryProvidedRX.subscribe {
            isCategoryProvided = it
            ifAllDataIsProvidedEnableSubmitButton()
        }
        isNameProvidedRX.subscribe {
            isNameProvided = it
            ifAllDataIsProvidedEnableSubmitButton()
        }
        isPriceProvidedRX.subscribe {
            isPriceProvided = it
            ifAllDataIsProvidedEnableSubmitButton()
        }
        isDescProvidedRX.subscribe {
            isDescProvided = it
            ifAllDataIsProvidedEnableSubmitButton()
        }


        return view
    }

    private fun showLoadingDialog(){
        alertDialog = AlertDialog.Builder(requireContext())
            .setView(ProgressBar(requireContext()))
            .setMessage("Adding your item...")
            .show()
    }

    private fun showSuccessDialog(){
        alertDialog = AlertDialog.Builder(requireContext())
            .setMessage("Your item was added successfully!")
            .setPositiveButton("Show added item",null)
            .setNegativeButton("Back",null)
            .show()

    alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
        TODO("When this is clicked [ItemFragment] with just added item should be opened, I'll leave this todo in here since I don't have [ItemFragment yet].")
    }
    }

    private fun endLoadingAndShowFailureDialog(){
        alertDialog = AlertDialog.Builder(requireContext())
            .setMessage("Oops.. something went wrong your item wasn't added.")
            .setNegativeButton("Back",null)
            .show()
    }

    private fun endLoadingAndContinue(){
        endLoading()
        clearAddedItemData()
        showSuccessDialog()
    }

    private fun clearAddedItemData(){
        binding.itemNameInput.text?.clear()
        binding.itemDescInput.text?.clear()
        binding.itemPriceInput.text?.clear()
        viewModel.clearCategory()
    }

    private fun endLoading(){
        alertDialog.dismiss()
    }

    private fun ifAllDataIsProvidedEnableSubmitButton() {
        if (isPictureProvided && isCategoryProvided && isNameProvided && isPriceProvided && isDescProvided){
            binding.submitItemButton.enable()
            clearErrorTextView()
        }
        else {
            binding.submitItemButton.disable()
            setSubmitErrorTextView()
        }
    }

    private fun clearErrorTextView(){
        binding.submitErrorTextView.text = ""
    }

    private fun setSubmitErrorTextView(){
        val list = listOf(isNameProvided,isDescProvided,isPriceProvided,isPictureProvided,isCategoryProvided).filter { !it }
        if(list.size > 1) binding.submitErrorTextView.text = getString(R.string.provideAllData)
        else binding.submitErrorTextView.text = findErrorMessage()
    }

    private fun findErrorMessage(): String{
        return when{
            !isNameProvided -> "Item name must be provided."
            !isDescProvided -> "Item description must be provided."
            !isPriceProvided -> "Item price must be provided."
            !isPictureProvided -> "Item must have at least 1 picture."
            !isCategoryProvided -> "Item must have category."
            else -> ""
        }
    }

    private fun addThisItemToFirebase(): Task<DocumentReference> {
        return viewModel.addItemToFirebase(
            binding.itemNameInput.text.toString(),
            binding.itemDescInput.text.toString(),
            binding.itemPriceInput.text.toString().toDouble(),
            99.9,
            "Used",
            "52"
            //binding.itemDeliveryCost.text.toString().toDouble(), // todo add this to UI and connect with this fields
        //binding.itemCondition.text.toString(),
        //binding.itemSize.text.toString()
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