package com.vashkpi.digitalretailgroup.adapters

import android.graphics.drawable.PictureDrawable
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.vashkpi.digitalretailgroup.R
import com.vashkpi.digitalretailgroup.data.models.domain.Brand
import com.vashkpi.digitalretailgroup.data.models.domain.Store
import com.vashkpi.digitalretailgroup.databinding.ItemStoreBinding
import com.vashkpi.digitalretailgroup.utils.GlideApp
import com.vashkpi.digitalretailgroup.utils.changeAlphaOnTouch

class StoresAdapter(
    private val clickListener: (View, Store) -> Unit
) : ListAdapter<Store, StoresViewHolder>(StoresAdapterDiffCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoresViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding =
            ItemStoreBinding.inflate(
                layoutInflater,
                parent,
                false
            )
        return StoresViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StoresViewHolder, position: Int) {
        holder.bind(getItem(position), clickListener)
    }

}

class StoresAdapterDiffCallBack : DiffUtil.ItemCallback<Store>() {
    override fun areItemsTheSame(oldItem: Store, newItem: Store): Boolean {
        return oldItem.store_id == newItem.store_id
    }

    override fun areContentsTheSame(oldItem: Store, newItem: Store): Boolean {
        return oldItem == newItem
    }
}

class StoresViewHolder(val binding: ItemStoreBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(data: Store, clickListener: (view: View, Store) -> Unit) {
        binding.root.changeAlphaOnTouch()
        binding.root.setOnClickListener {
            clickListener(it, data)
        }

        val picture = binding.logo

//        Glide
//            .with(picture)
//            .load(data.image_parth)
//            .placeholder(R.drawable.img_dummy_store_image)
//            .into(picture)

        val isSvg = data.image_parth.toString().endsWith(".svg")

        //if svg
        if (isSvg) {
            picture.scaleType = ImageView.ScaleType.CENTER_CROP
            GlideApp.with(picture)
                .`as`(PictureDrawable::class.java)
                //.placeholder(R.drawable.img_dummy_store_image)
                //.transition(DrawableTransitionOptions.withCrossFade())
                //.listener(SvgSoftwareLayerSetter())
                .load(data.image_parth)
                .into(picture)
        }
        //if .png or .jpg or others
        else {
            picture.scaleType = ImageView.ScaleType.CENTER_CROP
            Glide.with(picture)
                //.load("https://interactive-examples.mdn.mozilla.net/media/cc0-images/grapefruit-slice-332-332.jpg")
                .load(data.image_parth)
                .into(picture)
        }

        binding.name.text = data.name
        binding.address.text = data.address


    }
}