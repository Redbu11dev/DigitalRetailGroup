package com.vashkpi.digitalretailgroup.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vashkpi.digitalretailgroup.data.models.BrandInfoRegion
import com.vashkpi.digitalretailgroup.databinding.ItemBradInfoRegionBinding
import com.vashkpi.digitalretailgroup.utils.changeAlphaOnTouch

class BrandInfoAdapter(
    private val clickListener: (View, BrandInfoRegion) -> Unit
) : RecyclerView.Adapter<BrandInfoViewHolder>() {

    private val list = ArrayList<BrandInfoRegion>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BrandInfoViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding =
            ItemBradInfoRegionBinding.inflate(
                layoutInflater,
                parent,
                false
            )
        return BrandInfoViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: BrandInfoViewHolder, position: Int) {
        holder.bind(list[position], clickListener)
    }

    fun setList(items: List<BrandInfoRegion>) {
        list.clear()
        list.addAll(items)
        //clubsList.reverse()
    }

}

class BrandInfoViewHolder(val binding: ItemBradInfoRegionBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(data: BrandInfoRegion, clickListener: (view: View, BrandInfoRegion) -> Unit) {

        binding.root.changeAlphaOnTouch()
        binding.root.setOnClickListener {
            clickListener(it, data)
        }

        binding.text.text = data.name
    }
}