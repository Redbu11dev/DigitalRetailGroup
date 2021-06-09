package com.vashkpi.digitalretailgroup.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vashkpi.digitalretailgroup.databinding.ItemOptionBinding
import com.vashkpi.digitalretailgroup.databinding.PartnersListItemBinding
import com.vashkpi.digitalretailgroup.databinding.StoreListItemBinding

class StoresAdapter(
    private val clickListener: (View, String) -> Unit
) : RecyclerView.Adapter<StoresViewHolder>() {

    private val list = ArrayList<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoresViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding =
            StoreListItemBinding.inflate(
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

    fun setList(phrases: List<String>) {
        list.clear()
        list.addAll(phrases)
        //clubsList.reverse()
    }

}

class StoresViewHolder(val binding: StoreListItemBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(data: String, clickListener: (view: View, String) -> Unit) {
        binding.root.setOnClickListener {
            clickListener(it, data)
        }
//        binding.codeText.text = data.code
//        binding.nameText.text = data.name
    }
}