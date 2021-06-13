package com.vashkpi.digitalretailgroup.view

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Canvas
import android.graphics.LinearGradient
import android.graphics.Shader
import android.util.AttributeSet
import com.google.android.material.textview.MaterialTextView
import com.vashkpi.digitalretailgroup.R

/**
 * In order to work:
 * - view must have no horizontal padding
 * - width must me wrap_content or text should be horizontally centered
 */
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
            val gradientEndColor = it.getColorForState(drawableState, it.defaultColor)

            //println("state: ${drawableState.asList().toString()}")
            //[16842909, 16842910, 16842919, 16843547, 16843597]

//            android.util.StateSet
//            /** @hide */
//            public static final int VIEW_STATE_WINDOW_FOCUSED = 1;
//            /** @hide */
//            public static final int VIEW_STATE_SELECTED = 1 << 1;
//            /** @hide */
//            public static final int VIEW_STATE_FOCUSED = 1 << 2;
//            /** @hide */
//            public static final int VIEW_STATE_ENABLED = 1 << 3;
//            /** @hide */
//            public static final int VIEW_STATE_PRESSED = 1 << 4;
//            /** @hide */
//            public static final int VIEW_STATE_ACTIVATED = 1 << 5;
//            /** @hide */
//            public static final int VIEW_STATE_ACCELERATED = 1 << 6;
//            /** @hide */
//            public static final int VIEW_STATE_HOVERED = 1 << 7;
//            /** @hide */
//            public static final int VIEW_STATE_DRAG_CAN_ACCEPT = 1 << 8;
//            /** @hide */
//            public static final int VIEW_STATE_DRAG_HOVERED = 1 << 9;

            val textWidth = paint.measureText(text.toString())
            val left = (width/2f)-(textWidth/2f)
            mCurrentGradient = LinearGradient(
                left,
                0f,
                if (drawableState.size != 5) left+textWidth else left+(textWidth*0.25f),
                0f,
                currentTextColor,
                gradientEndColor,
                Shader.TileMode.CLAMP
            )
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