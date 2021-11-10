package com.pyn.criminalintent.utils

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Point
import android.util.DisplayMetrics

object PictureUtil {

    /**
     * 先确认屏幕的尺寸，然后按此缩放图像
     */
    fun getScaledBitmap(path: String, activity: Activity):Bitmap{
        val size = Point()
        val outMetrics = DisplayMetrics()
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            val display = activity.display
            display?.getRealMetrics(outMetrics)
        } else {
            @Suppress("DEPRECATION")
            val display = activity.windowManager.defaultDisplay
            @Suppress("DEPRECATION")
            display.getMetrics(outMetrics)
        }
        return getScaledBitmap(path,size.x,size.y)
    }

    fun getScaledBitmap(path: String, destWidth: Int, destHeigth: Int): Bitmap {
        var options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeFile(path, options)

        val srcWidth = options.outWidth.toFloat()
        val srcHeight = options.outHeight.toFloat()

        var inSampleSize = 1
        if (srcHeight > destHeigth || srcWidth > destWidth) {
            val heightScale = srcHeight / destHeigth
            val widthScale = srcWidth / destWidth

            val sampleScale = if (heightScale > widthScale) {
                heightScale
            } else {
                widthScale
            }
            inSampleSize = Math.round(sampleScale)
        }

        options = BitmapFactory.Options()
        options.inSampleSize = inSampleSize

        return BitmapFactory.decodeFile(path, options)
    }
}