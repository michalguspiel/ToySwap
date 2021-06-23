package com.erdees.toyswap.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.erdees.toyswap.databinding.FragmentItemBinding
import com.erdees.toyswap.model.models.item.Item
import com.erdees.toyswap.viewModel.ItemFragmentViewModel
import java.text.NumberFormat
import java.util.*

class ItemFragment : Fragment() {

    private var _binding : FragmentItemBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel : ItemFragmentViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentItemBinding.inflate(inflater,container,false)
        val view = binding.root
        viewModel = ViewModelProvider(this).get(ItemFragmentViewModel::class.java)

        viewModel.getPresentedItem().observe(viewLifecycleOwner,{ item ->
            if (item != null) {
                setBasicItemData(item)
                setPicturesRecyclerView(item)
                setSellerData(item.userId)
            }
        })


        return view
    }

    private fun setBasicItemData(item: Item) {
        binding.itemTitle.text  = item.name
        binding.itemDescription.text = item.description
        binding.itemPrice.text = NumberFormat.getCurrencyInstance(Locale.FRANCE).format(item.price)
        // TODO add category as additional layout into [additionalInformationLayout].
        // TODO add additional fields to [ITEM] (item condition,size?) and add it to same layout as category.

        // TODO add timestamp somewhere to the UI as when item was posted.

        // TODO add delivery price to ITEM as a field and fetch it to the UI
    }

    private fun setPicturesRecyclerView(item:Item) {
        // TODO make an array out of pics and set adapter to first recycler view which presents those pictures.
    }

    private fun setSellerData(userId: String){
        // TODO fetch seller data from firebase and set it to the UI.
    }

    companion object {
        const val TAG = "ItemFragment"
        fun newInstance() = ItemFragment()
    }

}