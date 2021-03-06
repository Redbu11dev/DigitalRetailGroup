package com.vashkpi.digitalretailgroup.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.daimajia.swipe.SwipeLayout
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter
import com.vashkpi.digitalretailgroup.R
import com.vashkpi.digitalretailgroup.data.models.domain.Notification
import com.vashkpi.digitalretailgroup.data.models.domain.formatDateToNotificationDate
import com.vashkpi.digitalretailgroup.databinding.ItemNotificationBinding
import com.vashkpi.digitalretailgroup.utils.changeAlphaOnTouch

class NotificationsAdapter(
    private val clickListener: (View, Notification) -> Unit,
    private val deleteBtnClickListener: (View, Notification) -> Unit
) : PagingDataAdapter<Notification, NotificationsViewHolder>(NotificationDiffCallBack()) {



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

    override fun onBindViewHolder(holder: NotificationsViewHolder, position: Int) {
        holder.bind(getItem(position), clickListener, deleteBtnClickListener)
    }

}

class NotificationDiffCallBack : DiffUtil.ItemCallback<Notification>() {
    override fun areItemsTheSame(oldItem: Notification, newItem: Notification): Boolean {
        return oldItem.notification_id == newItem.notification_id
    }

    override fun areContentsTheSame(oldItem: Notification, newItem: Notification): Boolean {
        return oldItem == newItem
    }
}

class NotificationsViewHolder(val binding: ItemNotificationBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(data: Notification?, clickListener: (view: View, Notification) -> Unit, deleteBtnClickListener: (View, Notification) -> Unit) {

        data?.let {
            if (data.read) {
                binding.visiblePart.setBackgroundResource(R.drawable.bgr_card)
            }
            else {
                binding.visiblePart.setBackgroundResource(R.drawable.bgr_card_light)
            }

            binding.title.text = data.title
            binding.text.text = data.text
            binding.date.text = data.date.formatDateToNotificationDate()

            binding.swipe.showMode = SwipeLayout.ShowMode.LayDown
            binding.swipe.addSwipeListener(object: SwipeLayout.SwipeListener{
                override fun onStartOpen(layout: SwipeLayout?) {
                    binding.visiblePart.isClickable = false
                }

                override fun onOpen(layout: SwipeLayout?) {

                }

                override fun onStartClose(layout: SwipeLayout?) {

                }

                override fun onClose(layout: SwipeLayout?) {

                }

                override fun onUpdate(layout: SwipeLayout?, leftOffset: Int, topOffset: Int) {
                    //mLeftOffset = leftOffset
                    println("left offset: $leftOffset")
                }

                override fun onHandRelease(layout: SwipeLayout?, xvel: Float, yvel: Float) {
                    binding.visiblePart.isClickable = true
                }

            })

            binding.visiblePart.changeAlphaOnTouch()
            binding.visiblePart.setOnClickListener {
                clickListener(it, data)
            }

            binding.icon.setOnClickListener {
                deleteBtnClickListener(it, data)
            }
        }
    }
}