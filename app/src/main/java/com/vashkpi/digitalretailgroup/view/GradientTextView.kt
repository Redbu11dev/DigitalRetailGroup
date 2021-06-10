package com.vashkpi.digitalretailgroup.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.LinearGradient
import android.graphics.Shader
import android.util.AttributeSet
import android.util.Log
import com.google.android.material.textview.MaterialTextView

class GradientTextView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : MaterialTextView(context, attrs, defStyleAttr) {

    val linearGradient = LinearGradient(
        0f,
        0f,
        width.toFloat(),
        0f,
        currentTextColor,
        currentHintTextColor,
        Shader.TileMode.CLAMP
    )

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
//        if (changed) {
//            paint.shader = linearGradient
//        }
//        Log.i("oof","oof: $currentTextColor")
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        //Log.i("oof","oof: $currentTextColor")
        val tst = textColors.getColorForState(drawableState, 0)
        Log.i("oof1","oof1: $tst")
        Log.i("oof","oof: $currentTextColor")
    }

}