package com.erdees.toyswap.view.adapters

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.erdees.toyswap.databinding.ItemPictureRvItemBinding

class ItemPicturesRvAdapter(private val picturesList : List<String>,val activity : Activity): RecyclerView.Adapter<ItemPicturesRvAdapter.ItemViewHolder>() {

    class ItemViewHolder(val viewBinding: ItemPictureRvItemBinding) : RecyclerView.ViewHolder(viewBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val layout = ItemPictureRvItemBinding.inflate(LayoutInflater.from(activity),parent,false)
        return ItemViewHolder(layout)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
    Glide.with(activity)
        .load(picturesList[position])
        .centerCrop()
        .into(holder.viewBinding.imageView)
    }

    override fun getItemCount(): Int = picturesList.size

}