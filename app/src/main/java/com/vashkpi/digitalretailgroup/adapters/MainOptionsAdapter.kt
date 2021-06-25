package com.vashkpi.digitalretailgroup.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.vashkpi.digitalretailgroup.R
import com.vashkpi.digitalretailgroup.data.models.domain.Brand
import com.vashkpi.digitalretailgroup.data.models.domain.Notification
import com.vashkpi.digitalretailgroup.databinding.ItemBrandBinding
import com.vashkpi.digitalretailgroup.databinding.ItemOptionBinding
import com.vashkpi.digitalretailgroup.utils.changeAlphaOnTouch

class MainOptionsAdapter(
    private val clickListener: (View, String) -> Unit
) : ListAdapter<String, MainOptionsViewHolder>(MainOptionsAdapterDiffCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainOptionsViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding =
            ItemOptionBinding.inflate(
                layoutInflater,
                parent,
                false
            )
        return MainOptionsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MainOptionsViewHolder, position: Int) {
        holder.bind(position, getItem(position), clickListener)
    }

}

class MainOptionsAdapterDiffCallBack : DiffUtil.ItemCallback<String>() {
    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }
}

class MainOptionsViewHolder(val binding: ItemOptionBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(position: Int, data: String, clickListener: (view: View, String) -> Unit) {
        binding.root.setOnClickListener {
            clickListener(it, data)
        }
        binding.root.changeAlphaOnTouch()

        //val refArray = binding.root.resources.getStringArray()

        val iconsArray = binding.root.resources.obtainTypedArray(R.array.main_options_array_icons)
        binding.text.text = data
        binding.icon.setImageResource(iconsArray.getResourceId(position, 0))
        iconsArray.recycle()

//        when (data) {
//            . -> {
//
//            }
//            else -> {
//
//            }
//        }

    }
}