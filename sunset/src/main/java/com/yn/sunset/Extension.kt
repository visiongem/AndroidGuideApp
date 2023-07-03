package com.yn.sunset

import android.content.res.Resources
import android.util.TypedValue


val Float.px
    get() = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX,this, Resources.getSystem().displayMetrics)

val Float.dp
    get() = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,this, Resources.getSystem().displayMetrics)

val Float.sp
    get() = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,this, Resources.getSystem().displayMetrics)

val Int.dp
    get() = this.toFloat().dp