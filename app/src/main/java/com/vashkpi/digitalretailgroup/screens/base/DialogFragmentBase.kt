package com.vashkpi.digitalretailgroup.screens.base

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment

abstract class DialogFragmentBase : DialogFragment() {
    override fun onStart() {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels * 1).toInt()
//        val height = (resources.displayMetrics.heightPixels * 1).toInt()
        dialog!!.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
//        dialog!!.window?.setLayout(width, height)
        dialog!!.window?.setGravity(Gravity.CENTER)
        dialog!!.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }
}