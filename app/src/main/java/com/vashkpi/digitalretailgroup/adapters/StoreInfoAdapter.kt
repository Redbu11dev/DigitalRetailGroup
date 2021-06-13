package com.vashkpi.digitalretailgroup.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vashkpi.digitalretailgroup.databinding.ItemOptionBinding

class StoreInfoAdapter(
    private val clickListener: (View, String) -> Unit
) : RecyclerView.Adapter<StoreInfoViewHolder>() {

    private val list = ArrayList<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoreInfoViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding =
            ItemOptionBinding.inflate(
                layoutInflater,
                parent,
                false
            )
        return StoreInfoViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: StoreInfoViewHolder, position: Int) {
        holder.bind(list[position], clickListener)
    }

    fun setList(phrases: List<String>) {
        list.clear()
        list.addAll(phrases)
        //clubsList.reverse()
    }

}

class StoreInfoViewHolder(val binding: ItemOptionBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(data: String, clickListener: (view: View, String) -> Unit) {
        binding.root.setOnClickListener {
            clickListener(it, data)
        }
//        binding.codeText.text = data.code
//        binding.nameText.text = data.name
    }
}