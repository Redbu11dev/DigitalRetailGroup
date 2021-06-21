package com.vashkpi.digitalretailgroup.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.vashkpi.digitalretailgroup.R
import com.vashkpi.digitalretailgroup.data.models.domain.Store
import com.vashkpi.digitalretailgroup.databinding.ItemStoreBinding
import com.vashkpi.digitalretailgroup.utils.changeAlphaOnTouch

class StoresAdapter(
    private val clickListener: (View, Store) -> Unit
) : RecyclerView.Adapter<StoresViewHolder>() {

    private val list = ArrayList<Store>()

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

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: StoresViewHolder, position: Int) {
        holder.bind(list[position], clickListener)
    }

    fun setList(items: List<Store>) {
        list.clear()
        list.addAll(items)
        //clubsList.reverse()
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

        Glide
            .with(picture)
            .load(data.image_parth)
            .placeholder(R.drawable.img_dummy_store_image)
            .into(picture)

        binding.name.text = data.name
        binding.address.text = data.address


    }
}