package com.vashkpi.digitalretailgroup.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.vashkpi.digitalretailgroup.data.models.domain.Brand
import com.vashkpi.digitalretailgroup.data.models.domain.BrandInfoRegion
import com.vashkpi.digitalretailgroup.databinding.ItemBrandInfoRegionBinding
import com.vashkpi.digitalretailgroup.utils.changeAlphaOnTouch

class BrandInfoAdapter(
    private val clickListener: (View, BrandInfoRegion) -> Unit
) : ListAdapter<BrandInfoRegion, BrandInfoViewHolder>(BrandInfoAdapterDiffCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BrandInfoViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding =
            ItemBrandInfoRegionBinding.inflate(
                layoutInflater,
                parent,
                false
            )
        return BrandInfoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BrandInfoViewHolder, position: Int) {
        holder.bind(getItem(position), clickListener)
    }

}

class BrandInfoAdapterDiffCallBack : DiffUtil.ItemCallback<BrandInfoRegion>() {
    override fun areItemsTheSame(oldItem: BrandInfoRegion, newItem: BrandInfoRegion): Boolean {
        return oldItem.region_id == newItem.region_id
    }

    override fun areContentsTheSame(oldItem: BrandInfoRegion, newItem: BrandInfoRegion): Boolean {
        return oldItem == newItem
    }
}

class BrandInfoViewHolder(val binding: ItemBrandInfoRegionBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(data: BrandInfoRegion, clickListener: (view: View, BrandInfoRegion) -> Unit) {

        binding.root.changeAlphaOnTouch()
        binding.root.setOnClickListener {
            clickListener(it, data)
        }

        binding.text.text = data.name
    }
}