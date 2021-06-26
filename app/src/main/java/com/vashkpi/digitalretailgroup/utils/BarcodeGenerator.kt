package com.vashkpi.digitalretailgroup.utils

import android.graphics.Bitmap
import android.widget.ImageView
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import com.google.zxing.BarcodeFormat
import com.google.zxing.oned.Code128Writer
import com.vashkpi.digitalretailgroup.R

class BarcodeGenerator {
    fun displayBitmap(imageView: ImageView, value: String) {
        val bitmap = createBarcodeBitmap(
            barcodeValue = value,
            barcodeColor = ContextCompat.getColor(imageView.context, R.color.one_layer_end),
            backgroundColor = ContextCompat.getColor(imageView.context, R.color.white),
            widthPixels = (imageView.resources.displayMetrics.widthPixels * 0.9f).toInt(),
            heightPixels = ((imageView.resources.displayMetrics.widthPixels * 0.9f) * 0.4f).toInt()
        )

        imageView.setImageBitmap(
            bitmap
        )
    }

    fun createBarcodeBitmap(
        barcodeValue: String,
        @ColorInt barcodeColor: Int,
        @ColorInt backgroundColor: Int,
        widthPixels: Int,
        heightPixels: Int
    ): Bitmap {
        val bitMatrix = Code128Writer().encode(
            barcodeValue,
            BarcodeFormat.CODE_128,
            widthPixels,
            heightPixels
        )

        val pixels = IntArray(bitMatrix.width * bitMatrix.height)
        for (y in 0 until bitMatrix.height) {
            val offset = y * bitMatrix.width
            for (x in 0 until bitMatrix.width) {
                pixels[offset + x] =
                    if (bitMatrix.get(x, y)) barcodeColor else backgroundColor
            }
        }

        val bitmap = Bitmap.createBitmap(
            bitMatrix.width,
            bitMatrix.height,
            Bitmap.Config.ARGB_8888
        )
        bitmap.setPixels(
            pixels,
            0,
            bitMatrix.width,
            0,
            0,
            bitMatrix.width,
            bitMatrix.height
        )
        return bitmap
    }
}