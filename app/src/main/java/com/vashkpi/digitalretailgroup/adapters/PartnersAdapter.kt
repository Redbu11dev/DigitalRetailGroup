package com.vashkpi.digitalretailgroup.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vashkpi.digitalretailgroup.databinding.ItemPartnerBinding
import com.vashkpi.digitalretailgroup.utils.changeAlphaOnTouch

class PartnersAdapter(
    private val clickListener: (View, String) -> Unit
) : RecyclerView.Adapter<PartnersViewHolder>() {

    private val list = ArrayList<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PartnersViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding =
            ItemPartnerBinding.inflate(
                layoutInflater,
                parent,
                false
            )
        return PartnersViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: PartnersViewHolder, position: Int) {
        holder.bind(list[position], clickListener)
    }

    fun setList(phrases: List<String>) {
        list.clear()
        list.addAll(phrases)
        //clubsList.reverse()
    }

}

class PartnersViewHolder(val binding: ItemPartnerBinding) :
    RecyclerView.ViewHolder(binding.root) {

    @SuppressLint("ClickableViewAccessibility")
    fun bind(data: String, clickListener: (view: View, String) -> Unit) {
        binding.root.setOnClickListener {
            clickListener(it, data)
        }
        binding.root.changeAlphaOnTouch()

//        binding.codeText.text = data.code
//        binding.nameText.text = data.name
    }
}