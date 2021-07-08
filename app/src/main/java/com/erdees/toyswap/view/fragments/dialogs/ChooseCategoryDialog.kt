package com.erdees.toyswap.view.fragments.dialogs

import android.graphics.drawable.Icon
import android.os.Bundle
import android.util.Log
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
import com.erdees.toyswap.model.models.item.ItemCategories
import com.erdees.toyswap.viewModel.ChooseCategoryDialogViewModel


class ChooseCategoryDialog : DialogFragment() {
    private var _binding: DialogChooseCategoryBinding? = null
    private val binding get() = _binding!!

    private lateinit var categories: ItemCategories
    private lateinit var viewModel: ChooseCategoryDialogViewModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogChooseCategoryBinding.inflate(inflater, container, false)
        val view = binding.root
        viewModel = ViewModelProvider(this).get(ChooseCategoryDialogViewModel::class.java)

        categories = ItemCategories()
        Log.i(TAG,categories.currentCategory.children!!.joinToString { it.parent?.categoryName.toString() })
        updateUi() // to initially draw categories.


        viewModel.categoryLiveData.ignoreFirst().observe(viewLifecycleOwner, { category ->
            if (category != null) this.dismiss()
        })

        return view
    }

    companion object {
        const val TAG = "ChooseCategoryDialog"
        fun newInstance(): ChooseCategoryDialog = ChooseCategoryDialog()
    }

    private fun updateUi() {
        binding.categoriesGridLayoutHolder.removeAllViews()
        for (eachChild in categories.currentCategory.children!!) {
            val eachCategoryCard =
                LayoutInflater.from(requireContext()).inflate(R.layout.category_card, null, false)
            eachCategoryCard.findViewById<TextView>(R.id.categoryCardHead).text =
                eachChild.categoryName
            if (eachChild.icon != null) setCategoryIcon(eachCategoryCard, eachChild.icon)
            eachCategoryCard.setOnClickListener {
                categories.pickCategory(eachChild)
                reactToCategoryPicked()
                binding.dialogChooseCategoryHead.text = categories.currentCategory.categoryName
            }
            binding.categoriesGridLayoutHolder.addView(eachCategoryCard)
        }

    }


    private fun setCategoryIcon(view: View, icon: Icon) {
      view.findViewById<ImageView>(R.id.categoryIcon).setImageIcon(icon)

    }

    private fun reactToCategoryPicked() {
        Log.i(TAG,"current category = ${categories.currentCategory}")
        if (categories.isCategoryFinal()) viewModel.setCategory(categories.currentCategory)
        else updateUi()
    }

}