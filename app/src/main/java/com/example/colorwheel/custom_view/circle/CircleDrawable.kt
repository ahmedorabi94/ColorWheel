package com.example.colorwheel.custom_view.circle

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import com.example.colorwheel.custom_view.utils.ensureWithinRange

class CircleDrawable {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply { strokeWidth = 0.2f }
    private var x = 0f
    private var y = 0f

    var indicatorColor = 0
    var strokeColor = 0
    var circleColor = 0
    var radius = 0

    var colorCircleScale = 0f
        set(value) { field = ensureWithinRange(value, 0f, 1f) }

    fun setCoordinates(x: Float, y: Float) {
        this.x = x
        this.y = y
    }

    fun draw(canvas: Canvas) {
        drawCircle(canvas)
        drawStroke(canvas)
        drawColorIndicator(canvas)
    }

    private fun drawCircle(canvas: Canvas) {
        paint.setShadowLayer(30f, 0f, 0f, Color.GRAY)
        paint.color = circleColor
        paint.style = Paint.Style.FILL
        canvas.drawCircle(x, y, radius.toFloat(), paint)
    }

    private fun drawStroke(canvas: Canvas) {
        val strokeCircleRadius = radius - paint.strokeWidth / 2f

        paint.color = strokeColor
        paint.style = Paint.Style.STROKE
        canvas.drawCircle(x, y, strokeCircleRadius, paint)
    }

    private fun drawColorIndicator(canvas: Canvas) {
        val colorIndicatorCircleRadius = radius * colorCircleScale

        paint.color = indicatorColor
        paint.style = Paint.Style.FILL
        canvas.drawCircle(x, y, colorIndicatorCircleRadius, paint)
    }

    fun restoreState(state: CircleState) {
        radius = state.radius
        circleColor = state.circleColor
        strokeColor = state.strokeColor
        colorCircleScale = state.colorCircleScale
    }

    fun saveState() = CircleState(this)
}