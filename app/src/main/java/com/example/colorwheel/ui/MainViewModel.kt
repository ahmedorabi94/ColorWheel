package com.example.colorwheel.ui

import android.graphics.Color
import androidx.lifecycle.ViewModel
import com.example.colorwheel.custom_view.utils.ColorSelector

class MainViewModel : ViewModel() {

    var firstSelectedColor = -1
    var secondSelectedColor = -1
    var thirdSelectedColor = -1

    var enum: ColorSelector? = null


    fun getSelectedColor(color: Int): Int {
        return Color.rgb(color shr 16 and 0xFF, color shr 8 and 0xFF, color and 0xFF)
    }
}