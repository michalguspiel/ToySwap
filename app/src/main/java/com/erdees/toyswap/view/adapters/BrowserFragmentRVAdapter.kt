package com.erdees.toyswap.view.adapters

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.erdees.toyswap.model.Item
import com.firebase.ui.firestore.paging.FirestorePagingAdapter
import com.firebase.ui.firestore.paging.FirestorePagingOptions

class BrowserFragmentRVAdapter(
    options: FirestorePagingOptions<Item>,
    val activity: Activity,
    val fm: FragmentManager
) : FirestorePagingAdapter<Item, BrowserFragmentRVAdapter.BrowserFragmentViewHolder>(options) {


    class BrowserFragmentViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BrowserFragmentViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(
        holder: BrowserFragmentViewHolder,
        position: Int,
        model: Item
    ) {
        TODO("Not yet implemented")
    }
}