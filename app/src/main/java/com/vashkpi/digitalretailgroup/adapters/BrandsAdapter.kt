package com.vashkpi.digitalretailgroup.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.vashkpi.digitalretailgroup.R
import com.vashkpi.digitalretailgroup.data.models.domain.Brand
import com.vashkpi.digitalretailgroup.data.models.domain.Notification
import com.vashkpi.digitalretailgroup.databinding.ItemBrandBinding
import com.vashkpi.digitalretailgroup.utils.changeAlphaOnTouch

class BrandsAdapter(
    private val clickListener: (View, Brand) -> Unit
) : ListAdapter<Brand, BrandsViewHolder>(BrandsAdapterDiffCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BrandsViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding =
            ItemBrandBinding.inflate(
                layoutInflater,
                parent,
                false
            )
        return BrandsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BrandsViewHolder, position: Int) {
        holder.bind(getItem(position), clickListener)
    }

}

class BrandsAdapterDiffCallBack : DiffUtil.ItemCallback<Brand>() {
    override fun areItemsTheSame(oldItem: Brand, newItem: Brand): Boolean {
        return oldItem.brand_id == newItem.brand_id
    }

    override fun areContentsTheSame(oldItem: Brand, newItem: Brand): Boolean {
        return oldItem == newItem
    }
}

class BrandsViewHolder(val binding: ItemBrandBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(data: Brand, clickListener: (view: View, Brand) -> Unit) {
        binding.root.setOnClickListener {
            clickListener(it, data)
        }
        binding.root.changeAlphaOnTouch()

        val logo = binding.logo

//        Glide
//            .with(logo)
//            .load(data.image_parth)
//            .placeholder(R.drawable.img_dummy_store_image)
//            .into(logo)

    }
}