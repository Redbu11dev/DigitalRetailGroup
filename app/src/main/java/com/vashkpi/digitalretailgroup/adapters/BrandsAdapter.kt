package com.vashkpi.digitalretailgroup.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.vashkpi.digitalretailgroup.R
import com.vashkpi.digitalretailgroup.data.models.incoming.Brand
import com.vashkpi.digitalretailgroup.databinding.ItemBrandBinding
import com.vashkpi.digitalretailgroup.utils.changeAlphaOnTouch

class BrandsAdapter(
    private val clickListener: (View, Brand) -> Unit
) : RecyclerView.Adapter<BrandsViewHolder>() {

    private val list = ArrayList<Brand>()

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

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: BrandsViewHolder, position: Int) {
        holder.bind(list[position], clickListener)
    }

    fun setList(items: List<Brand>) {
        list.clear()
        list.addAll(items)
        //clubsList.reverse()
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

        Glide
            .with(logo.context)
            .load("http://goo.gl/gEgYUd")
            .placeholder(R.drawable.img_dummy_store_image)
            .into(logo)

    }
}