package com.example.colorwheel.custom_view.view

import android.os.Parcel
import android.os.Parcelable
import android.view.View
import com.example.colorwheel.custom_view.circle.*
import com.example.colorwheel.custom_view.circle.readCircleState
import com.example.colorwheel.custom_view.extensions.readBooleanCompat
import com.example.colorwheel.custom_view.extensions.writeBooleanCompat

class ColorWheelState : View.BaseSavedState {

    val circleState: CircleState
    val interceptTouchEvent: Boolean
    val rgb: Int

    constructor(
        superState: Parcelable?,
        view: ColorWheel,
        thumbState: CircleState
    ) : super(superState) {
        this.circleState = thumbState
        interceptTouchEvent = view.interceptTouchEvent
        rgb = view.rgb
    }

    constructor(source: Parcel) : super(source) {
        circleState = source.readCircleState()
        interceptTouchEvent = source.readBooleanCompat()
        rgb = source.readInt()
    }

    override fun writeToParcel(out: Parcel, flags: Int) {
        super.writeToParcel(out, flags)
        out.writeCircleState(circleState, flags)
        out.writeBooleanCompat(interceptTouchEvent)
        out.writeInt(rgb)
    }

    companion object CREATOR : Parcelable.Creator<ColorWheelState> {

        override fun createFromParcel(source: Parcel) = ColorWheelState(source)

        override fun newArray(size: Int) = arrayOfNulls<ColorWheelState>(size)
    }
}