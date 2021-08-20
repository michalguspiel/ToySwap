package com.erdees.toyswap.view.fragments.dialogs

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.erdees.toyswap.R
import com.erdees.toyswap.databinding.DialogChooseCategoryBinding
import com.erdees.toyswap.model.Constants.STARTING_CATEGORY_ID
import com.erdees.toyswap.model.models.item.itemCategory.ItemCategory
import com.erdees.toyswap.model.utils.ItemCategoriesHandler
import com.erdees.toyswap.model.utils.Utils.ignoreFirst
import com.erdees.toyswap.model.utils.Utils.makeGone
import com.erdees.toyswap.model.utils.Utils.makeVisible
import com.erdees.toyswap.viewModel.ChooseCategoryDialogViewModel


class ChooseCategoryDialog : DialogFragment() {

    private var _binding: DialogChooseCategoryBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: ChooseCategoryDialogViewModel

    private lateinit var currentCategory: ItemCategory


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogChooseCategoryBinding.inflate(inflater, container, false)
        val view = binding.root
        viewModel = ViewModelProvider(this).get(ChooseCategoryDialogViewModel::class.java)


        viewModel.categoryLiveData.ignoreFirst().observe(viewLifecycleOwner, { category ->
            if (category != null) this.dismiss()
        })

        viewModel.categoriesHandlerLiveData.observe(viewLifecycleOwner, { categoriesHandler ->
            updateUi(categoriesHandler)
            currentCategory = categoriesHandler.currentCategory
            if (isCurrentCategoryMainCategory()) binding.chooseCategoryBackBtn.makeGone()
            else binding.chooseCategoryBackBtn.makeVisible()
            binding.dialogChooseCategoryHead.text =
                categoriesHandler.currentCategory.name
        })

        binding.chooseCategoryBackBtn.setOnClickListener {
            viewModel.pickPreviousCategory()
        }

        return view
    }

    companion object {
        const val TAG = "ChooseCategoryDialog"
        fun newInstance(): ChooseCategoryDialog = ChooseCategoryDialog()
    }


    private fun updateUi(categoriesHandler: ItemCategoriesHandler) {
        binding.categoriesGridLayoutHolder.removeAllViews()
        for (eachChild in categoriesHandler.currentCategoryChildren()) {
            val eachCategoryCard =
                LayoutInflater.from(requireContext()).inflate(R.layout.category_card, null, false)
            eachCategoryCard.findViewById<TextView>(R.id.categoryCardHead).text =
                eachChild.name
            if (!eachChild.iconRef.isNullOrBlank()) setCategoryIcon(eachCategoryCard,eachChild.icon)
            eachCategoryCard.setOnClickListener {
                viewModel.pickCategory(eachChild)
            }
            binding.categoriesGridLayoutHolder.addView(eachCategoryCard)

        }

    }

    private fun setCategoryIcon(view: View,iconId: Int?) {
        if(iconId != null ) {
            Glide.with(requireContext())
                .load(
                    ContextCompat.getDrawable(
                        requireContext(),
                        iconId
                    )
                )
                .into(view.findViewById(R.id.categoryIcon))
        }
        else Log.i(TAG,"NO ICON IN STATIC MAP")
        }

    override fun onCancel(dialog: DialogInterface) {
        viewModel.restartItemCategoriesHandler()
        super.onCancel(dialog)
    }

    override fun onDismiss(dialog: DialogInterface) {
        viewModel.restartItemCategoriesHandler()
        super.onDismiss(dialog)
    }

    private fun isCurrentCategoryMainCategory(): Boolean {
        return currentCategory.id == STARTING_CATEGORY_ID
    }

}