package com.example.colorwheel.custom_view.view


import android.content.Context
import android.content.res.TypedArray
import android.graphics.*
import android.graphics.drawable.GradientDrawable
import android.os.Parcelable
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import com.example.colorwheel.R
import com.example.colorwheel.custom_view.circle.CircleDrawable
import com.example.colorwheel.custom_view.utils.*
import com.example.colorwheel.custom_view.utils.isTap
import com.example.colorwheel.custom_view.utils.toDegrees
import com.example.colorwheel.custom_view.utils.toRadians
import kotlin.math.*

private val HUE_COLORS = intArrayOf(
    Color.RED,
    Color.YELLOW,
    Color.GREEN,
    Color.CYAN,
    Color.BLUE,
    Color.MAGENTA,
    Color.RED
)

private val SATURATION_COLORS = intArrayOf(
    Color.WHITE,
    setAlpha(Color.WHITE, 0)
)

open class ColorWheel @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val hueGradient = GradientDrawable().apply {
        gradientType = GradientDrawable.SWEEP_GRADIENT
        shape = GradientDrawable.OVAL
        colors = HUE_COLORS
    }

    private val saturationGradient = GradientDrawable().apply {
        gradientType = GradientDrawable.RADIAL_GRADIENT
        shape = GradientDrawable.OVAL
        colors = SATURATION_COLORS
    }

    private lateinit var coloristener : ColorChangeListener

    fun  setColorListener( listener : ColorChangeListener){
        coloristener = listener
    }

    private val circleDrawable = CircleDrawable()
    private val hsvColor = HsvColor(value = 1f)

    private var wheelCenterX = 0
    private var wheelCenterY = 0
    private var wheelRadius = 0
    private var downX = 0f
    private var downY = 0f

    var rgb
        get() = hsvColor.rgb
        set(rgb) {
            hsvColor.rgb = rgb
            hsvColor.set(value = 1f)
            fireColorListener()
            invalidate()
        }

    var circleRadius
        get() = circleDrawable.radius
        set(value) {
            circleDrawable.radius = value
            invalidate()
        }

    var circleColor
        get() = circleDrawable.circleColor
        set(value) {
            circleDrawable.circleColor = value
            invalidate()
        }

    var circleStrokeColor
        get() = circleDrawable.strokeColor
        set(value) {
            circleDrawable.strokeColor = value
            invalidate()
        }

    var circleColorCircleScale
        get() = circleDrawable.colorCircleScale
        set(value) {
            circleDrawable.colorCircleScale = value
            invalidate()
        }

    var interceptTouchEvent = true

    init {
        parseAttributes(context, attrs)
    }

    private fun parseAttributes(context: Context, attrs: AttributeSet?) {
        val array = context.obtainStyledAttributes(
            attrs,
            R.styleable.ColorWheel,
            0,
            R.style.ColorWheelDefaultStyle
        )
        readCircleRadius(array)
        readCircleColor(array)
        readStrokeColor(array)
        readColorCircleScale(array)
        array.recycle()
    }

    private fun readCircleRadius(array: TypedArray) {
        circleRadius = array.getDimensionPixelSize(R.styleable.ColorWheel_tb_circleRadius, 0)
    }

    private fun readCircleColor(array: TypedArray) {
        circleColor = array.getColor(R.styleable.ColorWheel_tb_circleColor, 0)

    }

    private fun readStrokeColor(array: TypedArray) {
        circleStrokeColor = array.getColor(R.styleable.ColorWheel_tb_circleStrokeColor, 0)
    }

    private fun readColorCircleScale(array: TypedArray) {
        circleColorCircleScale = array.getFloat(R.styleable.ColorWheel_tb_circleColorCircleScale, 0f)
    }

    fun setRgb(r: Int, g: Int, b: Int) {
        rgb = Color.rgb(r, g, b)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minDimension = minOf(
            MeasureSpec.getSize(widthMeasureSpec),
            MeasureSpec.getSize(heightMeasureSpec)
        )

        setMeasuredDimension(
            resolveSize(minDimension, widthMeasureSpec),
            resolveSize(minDimension, heightMeasureSpec)
        )
    }

    override fun onDraw(canvas: Canvas) {
        drawColorWheel(canvas)
        drawCircle(canvas)
    }

    private fun drawColorWheel(canvas: Canvas) {
        val hSpace = width - paddingLeft - paddingRight
        val vSpace = height - paddingTop - paddingBottom

        wheelCenterX = paddingLeft + hSpace / 2
        wheelCenterY = paddingTop + vSpace / 2
        wheelRadius = maxOf(minOf(hSpace, vSpace) / 2, 0)

        val left = wheelCenterX - wheelRadius
        val top = wheelCenterY - wheelRadius
        val right = wheelCenterX + wheelRadius
        val bottom = wheelCenterY + wheelRadius

        hueGradient.setBounds(left, top, right, bottom)
        saturationGradient.setBounds(left, top, right, bottom)
        saturationGradient.gradientRadius = wheelRadius.toFloat()

        hueGradient.draw(canvas)
        saturationGradient.draw(canvas)
    }

    private fun drawCircle(canvas: Canvas) {
        val r = hsvColor.saturation * wheelRadius
        val hueRadians = toRadians(hsvColor.hue)
        val x = cos(hueRadians) * r + wheelCenterX
        val y = sin(hueRadians) * r + wheelCenterY


        circleDrawable.indicatorColor = hsvColor.rgb
        circleDrawable.setCoordinates(x, y)
        circleDrawable.draw(canvas)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> onActionDown(event)
            MotionEvent.ACTION_MOVE -> updateColorOnMotionEvent(event)
            MotionEvent.ACTION_UP -> {
                updateColorOnMotionEvent(event)
                if (isTap(event, downX, downY)) performClick()
            }
        }

        return true
    }

    private fun onActionDown(event: MotionEvent) {
        parent.requestDisallowInterceptTouchEvent(interceptTouchEvent)
        updateColorOnMotionEvent(event)
        downX = event.x
        downY = event.y
    }

    override fun performClick() = super.performClick()

    private fun updateColorOnMotionEvent(event: MotionEvent) {
        calculateColor(event)
        fireColorListener()
        invalidate()
    }

    private fun calculateColor(event: MotionEvent) {
        val legX = event.x - wheelCenterX
        val legY = event.y - wheelCenterY
        val hypot = minOf(hypot(legX, legY), wheelRadius.toFloat())
        val hue = (toDegrees(atan2(legY, legX)) + 360) % 360
        val saturation = hypot / wheelRadius
        hsvColor.set(hue, saturation, 1f)
    }

    private fun fireColorListener() {
        //  colorChangeListener?.invoke(hsvColor.rgb)
        coloristener.setTintColor(hsvColor.rgb)
    }

    override fun onSaveInstanceState(): Parcelable {
        val superState = super.onSaveInstanceState()
        val thumbState = circleDrawable.saveState()
        return ColorWheelState(superState, this, thumbState)
    }

    override fun onRestoreInstanceState(state: Parcelable) {
        if (state is ColorWheelState) {
            super.onRestoreInstanceState(state.superState)
            readColorWheelState(state)
        } else {
            super.onRestoreInstanceState(state)
        }
    }

    private fun readColorWheelState(state: ColorWheelState) {
        circleDrawable.restoreState(state.circleState)
        interceptTouchEvent = state.interceptTouchEvent
        rgb = state.rgb
    }
}
