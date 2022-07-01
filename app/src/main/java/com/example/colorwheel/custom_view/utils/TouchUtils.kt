package com.example.colorwheel.custom_view.utils

import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import kotlin.math.hypot

fun View.isTap(lastEvent: MotionEvent, initialX: Float, initialY: Float): Boolean {
    val config = ViewConfiguration.get(context)
    val duration = lastEvent.eventTime - lastEvent.downTime
    val distance = hypot(lastEvent.x - initialX, lastEvent.y - initialY)
    return duration < ViewConfiguration.getTapTimeout() && distance < config.scaledTouchSlop
}
