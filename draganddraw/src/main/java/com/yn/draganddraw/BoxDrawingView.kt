package com.yn.draganddraw

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PointF
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View

/**
 * Description：
 * @author Created by pengyanni
 * @e-mail 393507488@qq.com
 * @time   2023/6/29 16:18
 */
private const val TAG = "BoxDrawingView"

class BoxDrawingView(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {

    private var currentBox: Box? = null
    private val boxen = mutableListOf<Box>()

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val current = PointF(event.x, event.y)
        var action = ""
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                action = "ACTION_DOWN"
                currentBox = Box(current).also { boxen.add(it) }
            }

            MotionEvent.ACTION_MOVE -> {
                action = "ACTION_MOVE"
                updateCurrentBox(current)
            }

            MotionEvent.ACTION_UP -> {
                action = "ACTION_UP"
                updateCurrentBox(current)
                currentBox = null
            }

            MotionEvent.ACTION_CANCEL -> {
                action = "ACTION_CANCEL"
                currentBox = null
            }
        }
        Log.i(TAG, "$action at x = ${current.x},y=${current.y}")
        return true
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
    }

    private fun updateCurrentBox(current: PointF) {
        currentBox?.let {
            it.end = current
            invalidate()
        }
    }
}