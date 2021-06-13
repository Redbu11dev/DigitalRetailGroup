package com.vashkpi.digitalretailgroup.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.daimajia.swipe.SwipeLayout
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter
import com.vashkpi.digitalretailgroup.R
import com.vashkpi.digitalretailgroup.databinding.ItemNotificationBinding
import com.vashkpi.digitalretailgroup.utils.changeAlphaOnTouch

class NotificationsAdapter(
    private val clickListener: (View, String) -> Unit
) : RecyclerSwipeAdapter<NotificationsViewHolder>() {

    private val list = ArrayList<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationsViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding =
            ItemNotificationBinding.inflate(
                layoutInflater,
                parent,
                false
            )
        return NotificationsViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun getSwipeLayoutResourceId(position: Int): Int {
        //TODO("Not yet implemented")
        return R.id.swipe
    }

    override fun onBindViewHolder(holder: NotificationsViewHolder, position: Int) {
        holder.bind(list[position], clickListener)
    }

    fun setList(phrases: List<String>) {
        list.clear()
        list.addAll(phrases)
        //clubsList.reverse()
    }

}

class NotificationsViewHolder(val binding: ItemNotificationBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(data: String, clickListener: (view: View, String) -> Unit) {

        binding.swipe.setShowMode(SwipeLayout.ShowMode.LayDown)
        binding.swipe.addSwipeListener(object: SwipeLayout.SwipeListener{
            override fun onStartOpen(layout: SwipeLayout?) {
                //TODO("Not yet implemented")
                binding.visiblePart.isClickable = false
            }

            override fun onOpen(layout: SwipeLayout?) {
                //TODO("Not yet implemented")
            }

            override fun onStartClose(layout: SwipeLayout?) {
                //TODO("Not yet implemented")
            }

            override fun onClose(layout: SwipeLayout?) {
                //TODO("Not yet implemented")
            }

            override fun onUpdate(layout: SwipeLayout?, leftOffset: Int, topOffset: Int) {
                //mLeftOffset = leftOffset
                println("left offset: $leftOffset")
                //TODO("Not yet implemented")
            }

            override fun onHandRelease(layout: SwipeLayout?, xvel: Float, yvel: Float) {
                //TODO("Not yet implemented")
                binding.visiblePart.isClickable = true
            }

        })

        binding.visiblePart.changeAlphaOnTouch()
        binding.visiblePart.setOnClickListener {
            clickListener(it, data)
        }
    }
}