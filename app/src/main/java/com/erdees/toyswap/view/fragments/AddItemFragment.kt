package com.erdees.toyswap.view.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.erdees.toyswap.databinding.FragmentAddItemBinding
import com.erdees.toyswap.model.models.item.ItemCategory
import com.erdees.toyswap.view.fragments.dialogs.ChooseCategoryDialog
import com.erdees.toyswap.viewModel.AddItemFragmentViewModel

class AddItemFragment : Fragment() {

    private var _binding : FragmentAddItemBinding? = null
    private val binding get() = _binding!!

    private lateinit var pickedCategory: ItemCategory

    private lateinit var viewModel : AddItemFragmentViewModel

    private val chooseCategoryDialog by lazy {
        ChooseCategoryDialog.newInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddItemBinding.inflate(inflater,container,false)
        val view = binding.root
        viewModel = ViewModelProvider(this).get(AddItemFragmentViewModel::class.java)


        viewModel.categoryLiveData.observe(viewLifecycleOwner,{
         if(it != null){
             Log.i(TAG,it.categoryName)
             pickedCategory = it
             binding.itemChosenCategory.text = it.categoryName
             binding.itemChooseCategoryBtn.text = "Change category"
         }
            if(it == null){
                binding.itemChosenCategory.text = ""
                binding.itemChooseCategoryBtn.text = "Pick category"
            }
        })

        binding.itemChooseCategoryBtn.setOnClickListener {
            chooseCategoryDialog.show(childFragmentManager,ChooseCategoryDialog.TAG)
        }

        return view
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