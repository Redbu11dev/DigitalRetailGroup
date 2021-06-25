package com.vashkpi.digitalretailgroup.adapters

import android.graphics.drawable.PictureDrawable
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.bumptech.glide.request.target.Target
import com.vashkpi.digitalretailgroup.R
import com.vashkpi.digitalretailgroup.data.models.domain.Brand
import com.vashkpi.digitalretailgroup.databinding.ItemBrandBinding
import com.vashkpi.digitalretailgroup.utils.GlideApp
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

        //val requestBuilder = Glide.with(logo).load(data.image_parth).us

        GlideApp.with(logo)
            .`as`(PictureDrawable::class.java)
            //.placeholder(R.drawable.img_dummy_store_image)
            //.error(R.drawable.img_dummy_store_image)
            .transition(withCrossFade())
            //.listener(SvgSoftwareLayerSetter())
            .load(Uri.parse(data.image_parth))
            .into(logo)

//        Glide
//            .with(logo)
//            .`as`(PictureDrawable::class.java)
//            .load(data.image_parth)
//            //.placeholder(R.drawable.img_dummy_store_image)
//            .into(logo)

    }
}