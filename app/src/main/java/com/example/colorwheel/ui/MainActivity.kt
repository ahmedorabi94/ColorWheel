package com.example.colorwheel.ui

import android.content.res.ColorStateList
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.colorwheel.R
import com.example.colorwheel.custom_view.utils.ColorChangeListener
import com.example.colorwheel.custom_view.utils.ColorSelector
import com.example.colorwheel.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity(), ColorChangeListener {


    private val viewModel: MainViewModel by viewModels()
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.colorWheel.setColorListener(this)
        binding.fab1.setOnClickListener {
            viewModel.enum = ColorSelector.First
            if (viewModel.firstSelectedColor != -1) {
                binding.colorWheel.rgb = viewModel.firstSelectedColor
            } else {
                binding.colorWheel.rgb = getColor(R.color.teal)
            }
            binding.frameLayout1.setBackgroundColor(getColor(R.color.selected_bk_ground_color))

            binding.frameLayout2.setBackgroundColor(getColor(R.color.bk_ground_color))
            binding.frameLayout3.setBackgroundColor(getColor(R.color.bk_ground_color))

        }

        binding.fab2.setOnClickListener {
            viewModel.enum = ColorSelector.Second
            if (viewModel.secondSelectedColor != -1) {
                binding.colorWheel.rgb = viewModel.secondSelectedColor
            } else {
                binding.colorWheel.rgb = getColor(R.color.green)
            }
            binding.frameLayout2.setBackgroundColor(getColor(R.color.selected_bk_ground_color))

            binding.frameLayout1.setBackgroundColor(getColor(R.color.bk_ground_color))
            binding.frameLayout3.setBackgroundColor(getColor(R.color.bk_ground_color))
        }

        binding.fab3.setOnClickListener {
            viewModel.enum = ColorSelector.Third
            if (viewModel.thirdSelectedColor != -1) {
                binding.colorWheel.rgb = viewModel.thirdSelectedColor
            } else {
                binding.colorWheel.rgb = getColor(R.color.orange)
            }
            binding.frameLayout3.setBackgroundColor(getColor(R.color.selected_bk_ground_color))

            binding.frameLayout1.setBackgroundColor(getColor(R.color.bk_ground_color))
            binding.frameLayout2.setBackgroundColor(getColor(R.color.bk_ground_color))
        }

    }
    override fun setTintColor(color: Int) {

        val c = viewModel.getSelectedColor(color)

        when (viewModel.enum) {
            ColorSelector.First -> {
                viewModel.firstSelectedColor = c
                binding.fab1.backgroundTintList = ColorStateList.valueOf(c)
            }
            ColorSelector.Second -> {
                viewModel.secondSelectedColor = c
                binding.fab2.backgroundTintList = ColorStateList.valueOf(c)
            }
            ColorSelector.Third -> {
                viewModel.thirdSelectedColor = c
                binding.fab3.backgroundTintList = ColorStateList.valueOf(c)
            }
        }

    }

}