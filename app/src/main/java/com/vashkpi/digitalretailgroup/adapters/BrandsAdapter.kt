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

        val isSvg = data.image_parth.toString().endsWith(".svg")

        //if svg
        if (isSvg) {
            logo.scaleType = ImageView.ScaleType.FIT_XY
            GlideApp.with(logo)
                .`as`(PictureDrawable::class.java)
                //.placeholder(R.drawable.img_dummy_store_image)
                //.transition(withCrossFade())
                //.listener(SvgSoftwareLayerSetter())
                .load(data.image_parth)
                .into(logo)
        }
        //if .png or .jpg or others
        else {
            logo.scaleType = ImageView.ScaleType.FIT_CENTER
            Glide.with(logo)
            //.load("https://interactive-examples.mdn.mozilla.net/media/cc0-images/grapefruit-slice-332-332.jpg")
            .load(data.image_parth)
            .into(logo)
        }


//        Glide
//            .with(logo)
//            .`as`(PictureDrawable::class.java)
//            .load(data.image_parth)
//            //.placeholder(R.drawable.img_dummy_store_image)
//            .into(logo)

    }
}