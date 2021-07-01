package com.erdees.toyswap.view.adapters

import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.erdees.toyswap.Utils
import com.erdees.toyswap.databinding.ItemRecyclerItemBinding
import com.erdees.toyswap.model.models.item.Item
import com.erdees.toyswap.view.fragments.ItemFragment
import com.erdees.toyswap.viewModel.BrowserFragmentRvAdapterViewModel
import com.firebase.ui.firestore.paging.FirestorePagingAdapter
import com.firebase.ui.firestore.paging.FirestorePagingOptions
import java.util.*

class BrowserFragmentRVAdapter(
    options: FirestorePagingOptions<Item>,
    private val activity: Activity,
    private val fm: FragmentManager,
    val viewModel : BrowserFragmentRvAdapterViewModel
) : FirestorePagingAdapter<Item, BrowserFragmentRVAdapter.BrowserFragmentViewHolder>(options) {

    class BrowserFragmentViewHolder(val viewBinding: ItemRecyclerItemBinding) : RecyclerView.ViewHolder(viewBinding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BrowserFragmentViewHolder {
        val view = ItemRecyclerItemBinding.inflate(LayoutInflater.from(activity),parent,false)
       return BrowserFragmentViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: BrowserFragmentViewHolder,
        position: Int,
        model: Item
    ) {
        Log.i(TAG , "${model.name} , $model")
        holder.viewBinding.titleTV.text = model.name
        holder.viewBinding.price.text = java.text.NumberFormat.getCurrencyInstance(Locale.FRENCH).format(model.price)
        Glide.with(activity)
            .load(model.mainImageUrl)
            .centerCrop()
            .into(holder.viewBinding.itemPicture)

        holder.viewBinding.itemWholeLayout.setOnClickListener {
            Log.i(TAG,"ITEM CLICKED")
        viewModel.setItemToPresent(model)
            Utils.openFragment(ItemFragment.newInstance(),ItemFragment.TAG,fm)
        }
    }

    companion object {
        const val TAG = "BrowserRvAdapter"
    }
}