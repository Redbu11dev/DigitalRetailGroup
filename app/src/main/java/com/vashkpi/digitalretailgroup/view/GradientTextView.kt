package com.vashkpi.digitalretailgroup.view

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Canvas
import android.graphics.LinearGradient
import android.graphics.Shader
import android.util.AttributeSet
import com.google.android.material.textview.MaterialTextView
import com.vashkpi.digitalretailgroup.R

class GradientTextView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : MaterialTextView(context, attrs, defStyleAttr) {

    private var mGradientSecondaryColorStateList: ColorStateList? = null
    private lateinit var mCurrentGradient: LinearGradient

    init {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.GradientTextView, 0, 0)
        try {
            //try to get the color state list
            mGradientSecondaryColorStateList = ta.getColorStateList(R.styleable.GradientTextView_gtvSecondaryColor)
            updateGradient()
        }
        catch (t: Throwable) {
            //rip
        }
        finally {
            ta.recycle()
        }
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        //can not use "if (changed)" because then it will not update in the xml preview
        updateGradient()
    }

    private fun updateGradient() {
        mGradientSecondaryColorStateList?.let {
            val gradientEndColor = it.getColorForState(drawableState, 0)

            val newGradient = LinearGradient(
                0f,
                0f,
                width.toFloat(),
                0f,
                currentTextColor,
                gradientEndColor,
                Shader.TileMode.CLAMP
            )

            mCurrentGradient = newGradient
        }
    }

    override fun setTextColor(color: Int) {
        super.setTextColor(color)

        updateGradient()
    }

    override fun setTextColor(colors: ColorStateList?) {
        super.setTextColor(colors)

        updateGradient()
    }

    override fun drawableStateChanged() {
        super.drawableStateChanged()

        updateGradient()
    }

    override fun onDraw(canvas: Canvas?) {
        mGradientSecondaryColorStateList?.let {
            paint.shader = mCurrentGradient
        }

        super.onDraw(canvas)
    }

}