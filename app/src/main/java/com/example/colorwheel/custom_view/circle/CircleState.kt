package com.example.colorwheel.custom_view.circle

import android.os.Parcel
import android.os.Parcelable

class CircleState private constructor(
    val radius: Int,
    val circleColor: Int,
    val strokeColor: Int,
    val colorCircleScale: Float
) : Parcelable {

    constructor(circleDrawable: CircleDrawable) : this(
        circleDrawable.radius,
        circleDrawable.circleColor,
        circleDrawable.strokeColor,
        circleDrawable.colorCircleScale
    )

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readFloat()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(radius)
        parcel.writeInt(circleColor)
        parcel.writeInt(strokeColor)
        parcel.writeFloat(colorCircleScale)
    }

    override fun describeContents() = 0

    companion object {

        val EMPTY_STATE = CircleState(0, 0, 0, 0f)

        @JvmField
        val CREATOR = object : Parcelable.Creator<CircleState> {

            override fun createFromParcel(parcel: Parcel) = CircleState(parcel)

            override fun newArray(size: Int) = arrayOfNulls<CircleState>(size)
        }
    }
}

internal fun Parcel.writeCircleState(state: CircleState, flags: Int) {
    this.writeParcelable(state, flags)
}

internal fun Parcel.readCircleState(): CircleState {
    return this.readParcelable(CircleState::class.java.classLoader)
        ?: CircleState.EMPTY_STATE
}
