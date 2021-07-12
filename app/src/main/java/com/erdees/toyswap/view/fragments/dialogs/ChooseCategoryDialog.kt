package com.erdees.toyswap.view.fragments.dialogs

import android.content.DialogInterface
import android.graphics.drawable.Icon
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.erdees.toyswap.R
import com.erdees.toyswap.Utils.ignoreFirst
import com.erdees.toyswap.databinding.DialogChooseCategoryBinding
import com.erdees.toyswap.model.models.item.ItemCategoriesHandler
import com.erdees.toyswap.viewModel.ChooseCategoryDialogViewModel

/**
 * TODO OVERRIDE BACK BUTTON WHEN THIS IS SHOWN AND USER PRESSES BACK BUTTON PREVIOUS CATEGORY MUST BE PICKED IF THERE IS NOT ANY THEN SUPER.ONBACKPRESSED.
 * */


class ChooseCategoryDialog : DialogFragment() {
    private var _binding: DialogChooseCategoryBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: ChooseCategoryDialogViewModel


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

        viewModel.categoriesLiveData.observe(viewLifecycleOwner,{ itemCategories ->
            updateUi(itemCategories)
        })



        return view
    }

    companion object {
        const val TAG = "ChooseCategoryDialog"
        fun newInstance(): ChooseCategoryDialog = ChooseCategoryDialog()
    }

    private fun updateUi(categoriesHandler: ItemCategoriesHandler) {
        binding.categoriesGridLayoutHolder.removeAllViews()
        for (eachChild in categoriesHandler.currentCategory.children!!) {
            val eachCategoryCard =
                LayoutInflater.from(requireContext()).inflate(R.layout.category_card, null, false)
            eachCategoryCard.findViewById<TextView>(R.id.categoryCardHead).text =
                eachChild.categoryName
            if (eachChild.icon != null) setCategoryIcon(eachCategoryCard, eachChild.icon)
            eachCategoryCard.setOnClickListener {
                viewModel.pickCategory(eachChild)
                binding.dialogChooseCategoryHead.text = categoriesHandler.currentCategory.categoryName
            }
            binding.categoriesGridLayoutHolder.addView(eachCategoryCard)
        }

    }

    private fun setCategoryIcon(view: View, icon: Icon) {
      view.findViewById<ImageView>(R.id.categoryIcon).setImageIcon(icon)
    }

    override fun onDismiss(dialog: DialogInterface) {
        viewModel.clearItemCategoriesHandler()
        super.onDismiss(dialog)
    }


}