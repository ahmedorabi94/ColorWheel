package com.example.colorwheel.custom_view.utils

import android.graphics.Color

fun setAlpha(argb: Int, alpha: Int) =
    Color.argb(alpha, Color.red(argb), Color.green(argb), Color.blue(argb))