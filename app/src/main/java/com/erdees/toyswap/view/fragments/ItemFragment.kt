package com.erdees.toyswap.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.erdees.toyswap.R
import com.erdees.toyswap.databinding.FragmentItemBinding
import com.erdees.toyswap.databinding.ItemAdditionalInfoBoxBinding
import com.erdees.toyswap.model.models.item.Item
import com.erdees.toyswap.viewModel.ItemFragmentViewModel
import java.text.NumberFormat
import java.util.*

class ItemFragment : Fragment() {

    private var _binding: FragmentItemBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: ItemFragmentViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentItemBinding.inflate(inflater, container, false)
        val view = binding.root
        viewModel = ViewModelProvider(this).get(ItemFragmentViewModel::class.java)

        viewModel.getPresentedItem().observe(viewLifecycleOwner, { item ->
            if (item != null) {
                setBasicItemData(item)
                setPicturesRecyclerView(item)
                setSellerData(item.userId)
            }
        })


        return view
    }

    private fun setBasicItemData(item: Item) {
        binding.itemTitle.text = item.name
        binding.itemDescription.text = item.description
        binding.itemPrice.text = NumberFormat.getCurrencyInstance(Locale.FRANCE).format(item.price)
        setItemAdditionalInformation(item)
        binding.itemTimestamp.text = item.timeStamp.toDate().toString()
        binding.deliveryPrice.text = NumberFormat.getCurrencyInstance(Locale.FRANCE).format(item.deliveryCost)
    }

    private fun setItemAdditionalInformation(item: Item) {
        addItemConditionToBox(item)
        addItemCategoryToBox(item)
        if (item.size.isNotBlank()) addItemSizeToBox(item)
    }

    private fun addItemCategoryToBox(item:Item){
        val layout =
            LayoutInflater.from(requireContext()).inflate(R.layout.item_additional_info_box,null,false)
        val thisBinding = ItemAdditionalInfoBoxBinding.bind(layout)
        item.category.get().addOnSuccessListener {
            val categoryName = it["categoryName"].toString()
            if(categoryName.isNotBlank() && categoryName != "null") {
                thisBinding.itemAdditionalInfoBoxTV.text = categoryName
                binding.additionalInformationLayout.addView(layout)
            }
        }
    }

    private fun addItemConditionToBox(item: Item) {
        val layout =
            LayoutInflater.from(requireContext()).inflate(R.layout.item_additional_info_box,null,false)
        val thisBinding = ItemAdditionalInfoBoxBinding.bind(layout)
        thisBinding.itemAdditionalInfoBoxTV.text = "${item.itemCondition}"
        binding.additionalInformationLayout.addView(layout)
    }

    private fun addItemSizeToBox(item: Item) {
        val layout =
            LayoutInflater.from(requireContext()).inflate(R.layout.item_additional_info_box,null,false)
        val thisBinding = ItemAdditionalInfoBoxBinding.bind(layout)
        thisBinding.itemAdditionalInfoBoxTV.text = "Size ${item.size}"
        binding.additionalInformationLayout.addView(layout)
    }

    private fun setPicturesRecyclerView(item: Item) {
        // TODO make an array out of pics and set adapter to first recycler view which presents those pictures.
    }

    private fun setSellerData(userId: String) {
        // TODO fetch seller data from firebase and set it to the UI.
    }

    companion object {
        const val TAG = "ItemFragment"
        fun newInstance() = ItemFragment()
    }

}