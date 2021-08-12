package com.erdees.toyswap.view.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.erdees.toyswap.R
import com.erdees.toyswap.Utils.makeGone
import com.erdees.toyswap.Utils.makeVisible
import com.erdees.toyswap.databinding.FragmentItemBinding
import com.erdees.toyswap.databinding.ItemAdditionalInfoBoxBinding
import com.erdees.toyswap.model.Constants
import com.erdees.toyswap.model.Reputation
import com.erdees.toyswap.model.models.item.Item
import com.erdees.toyswap.model.models.user.PublicUserData
import com.erdees.toyswap.view.adapters.ItemPicturesRvAdapter
import com.erdees.toyswap.viewModel.ItemFragmentViewModel
import java.text.NumberFormat
import java.util.*

class ItemFragment : Fragment() {

    /**TODO : Making an offer feature,
     *  buying with delivery?(no idea how this will work),
     *  implement recycler on bottom where user sees other items*/

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
                getSellerData(item.userId)
            }
        })


        return view
    }

    private fun setBasicItemData(item: Item) {
        binding.itemTitle.text = item.name
        binding.itemDescription.text = item.description
        binding.itemPrice.text = NumberFormat.getCurrencyInstance(Locale.FRANCE).format(item.price)
        setItemAdditionalInformation(item)
        binding.itemTimestamp.text = DateFormat.format(
            "dd-MM-yyyy", item.timeStamp.toDate()
        )
        setPickupOptions(item)
    }

    private fun setPickupOptions(item: Item) {
        if (item.deliveryCost != null) binding.deliveryPrice.text =
            NumberFormat.getCurrencyInstance(Locale.FRANCE).format(item.deliveryCost)
        val itemPickupOptions = item.pickupOptions.map { it.name }
        if (itemPickupOptions.contains(Constants.personalPickup.name)) binding.makeAnOfferBtn.makeVisible()
        else binding.makeAnOfferBtn.makeGone()
        if (itemPickupOptions.contains(Constants.shipment.name)) binding.buyWithDeliveryLayout.makeVisible()
        else binding.buyWithDeliveryLayout.makeGone()
    }


    private fun setItemAdditionalInformation(item: Item) {
        addItemConditionToBox(item)
        addItemCategoryToBox(item)
        if (item.size.isNotBlank()) addItemSizeToBox(item)
    }

    private fun addItemCategoryToBox(item: Item) {
        val layout =
            LayoutInflater.from(requireContext())
                .inflate(R.layout.item_additional_info_box, null, false)
        val thisBinding = ItemAdditionalInfoBoxBinding.bind(layout)
        val categoryName = item.category.categoryName
        if (categoryName.isNotBlank() && categoryName != "null") {
            thisBinding.itemAdditionalInfoBoxTV.text = categoryName
            binding.additionalInformationLayout.addView(layout)
        }
    }

    private fun addItemConditionToBox(item: Item) {
        val layout =
            LayoutInflater.from(requireContext())
                .inflate(R.layout.item_additional_info_box, null, false)
        val thisBinding = ItemAdditionalInfoBoxBinding.bind(layout)
        thisBinding.itemAdditionalInfoBoxTV.text = "${item.itemCondition}"
        binding.additionalInformationLayout.addView(layout)
    }

    private fun addItemSizeToBox(item: Item) {
        val layout =
            LayoutInflater.from(requireContext())
                .inflate(R.layout.item_additional_info_box, null, false)
        val thisBinding = ItemAdditionalInfoBoxBinding.bind(layout)
        thisBinding.itemAdditionalInfoBoxTV.text = "Size ${item.size}"
        binding.additionalInformationLayout.addView(layout)
    }

    private fun setPicturesRecyclerView(item: Item) {
        val picList = listOf(item.mainImageUrl) + item.otherImagesUrl
        val adapter = ItemPicturesRvAdapter(picList, requireActivity())
        binding.itemPicturesRv.adapter = adapter
        binding.itemPicturesRv.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
    }

    private fun getSellerData(userId: String) {
        var seller: PublicUserData?
        seller = viewModel.getSellerData(userId)
        if (seller != null) setSellerData(seller)
        else viewModel.downloadSellerFromServer(userId).addOnSuccessListener {
            seller = viewModel.getSellerData(userId)
            setSellerData(seller)
        }
            .addOnFailureListener {
                Log.i(TAG, "Handle error here.")
            }
    }

    @SuppressLint("SetTextI18n")
    private fun setSellerData(seller: PublicUserData?) {
        if (seller != null) {
            binding.sellerName.text = seller.firstName
            binding.sellerCity.text = seller.addressCity
            binding.sellerReputation.text = Reputation(seller.reputation).reputationDesc
            binding.sellerExperience.text =
                getString(R.string.in_toyswap_since) + " " + DateFormat.format(
                    "yyyy",
                    seller.accCreationTimeStamp.toDate()
                )
            Glide.with(requireContext())
                .load(seller.avatar)
                .circleCrop()
                .into(binding.sellerAvatar)
        } else {
            Log.i(TAG, "Handle error here.")
        }
    }

    companion object {
        const val TAG = "ItemFragment"
        fun newInstance() = ItemFragment()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}