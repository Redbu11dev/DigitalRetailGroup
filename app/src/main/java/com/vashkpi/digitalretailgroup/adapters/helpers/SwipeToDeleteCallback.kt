package com.vashkpi.digitalretailgroup.adapters.helpers

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Vibrator
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.vashkpi.digitalretailgroup.R

class SwipeToDeleteCallback(
    var context: Context
) :
    ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

    private val background: ColorDrawable =
        ColorDrawable(ContextCompat.getColor(context, R.color.transparent))
//    private val backgroundColorSafe = Color.BLUE
//    private val backgroundColorUnsafe = Color.RED
    private var icon: Drawable =
        ContextCompat.getDrawable(context, R.drawable.ic_trash_orange)!!

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        return makeMovementFlags(0, ItemTouchHelper.START);
        //return super.getMovementFlags(recyclerView, viewHolder)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        viewHolder2: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDirection: Int) {
       // myAdapter.removeItem(viewHolder.adapterPosition, viewHolder)
    }

    //var canVibrate = true

    override fun onChildDraw(
        canvas: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        val itemView = viewHolder.itemView
        val backgroundCornerOffset = 20
        val iconMarginVertical = (viewHolder.itemView.height - icon.intrinsicHeight) / 2
        val iconMarginHorizontal = ((icon.intrinsicHeight) * 1f).toInt()

//        icon.setBounds(
//            itemView.right - iconMarginHorizontal - icon.intrinsicWidth,
//            itemView.top + iconMarginVertical,
//            itemView.right - iconMarginHorizontal,
//            itemView.bottom - iconMarginVertical
//        )
//        icon.level = 0

        when {
//            dX > 0 -> { // Swiping to the right
//                icon.setBounds(
//                    itemView.left + iconMarginHorizontal,
//                    itemView.top + iconMarginVertical,
//                    itemView.left + iconMarginHorizontal + icon.intrinsicWidth,
//                    itemView.bottom - iconMarginVertical
//                )
//
//                background.setBounds(
//                    itemView.left,
//                    itemView.top,
//                    itemView.left + dX.toInt() + backgroundCornerOffset, //or itemView.w instead of dX
//                    itemView.bottom
//                )
//            }
            dX < 0 -> { // Swiping to the left
                background.setBounds(
                    itemView.right + dX.toInt() - backgroundCornerOffset, //or itemView.w instead of dX
                    itemView.top,
                    itemView.right,
                    itemView.bottom
                )

                //fixme find out correct value
                if ((background.bounds.left + backgroundCornerOffset) < itemView.right / 2) {
                    //background.color = backgroundColorUnsafe
//                    if (canVibrate) {
//                        val vb = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator?
//                        vb!!.vibrate(100)
//                        canVibrate = false
//                    }
                } else {
                    //canVibrate = true
                    //background.color = backgroundColorSafe
                }

                if ((background.bounds.left + backgroundCornerOffset) < (itemView.right - iconMarginHorizontal - icon.intrinsicWidth)) {
                    icon.setBounds(
                        itemView.right - iconMarginHorizontal - icon.intrinsicWidth,
                        itemView.top + iconMarginVertical,
                        itemView.right - iconMarginHorizontal,
                        itemView.bottom - iconMarginVertical
                    )
                }
                else {
                    icon.setBounds(0, 0, 0, 0)
                }
            }
            else -> { // view is unSwiped
                icon.setBounds(0, 0, 0, 0)
                background.setBounds(0, 0, 0, 0)
            }
        }

        background.draw(canvas)
        icon.draw(canvas)

        super.onChildDraw(
            canvas,
            recyclerView,
            viewHolder,
            dX,
            dY,
            actionState,
            isCurrentlyActive
        )
    }
}