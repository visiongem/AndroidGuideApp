package com.yn.draganddraw

import android.graphics.PointF
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Description：
 * @author Created by pengyanni
 * @e-mail 393507488@qq.com
 * @time   2023/6/29 17:00
 */
@Parcelize
class Box(val start: PointF): Parcelable {

    var end: PointF = start

    val left: Float get() = Math.min(start.x, end.x)

    val right: Float get() = Math.max(start.x, end.x)

    val top: Float get() = Math.min(start.y, end.y)

    val bottom: Float get() = Math.max(start.y, end.y)
}