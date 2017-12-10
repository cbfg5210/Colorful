package com.ue.colorful.feature.pickphoto

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.ue.colorful.R
import com.ue.colorful.event.ColorListener
import kotlinx.android.synthetic.main.activity_photo_color_picker.*

class PhotoColorPickerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_color_picker)

        colorPickerView.setColorListener(object : ColorListener {
            override fun onColorSelected(color: Int) {
                textView.text = "#" + String.format("%06X", 0xFFFFFF and color)
                linearLayout.setBackgroundColor(color)
            }
        })
    }

    fun onViewClick(v: View) {
        when (v.id) {
            R.id.ivThumbToggle -> {
                ivThumbToggle.isSelected = !ivThumbToggle.isSelected
                colorPickerView.toggleThumbColor()
            }
            R.id.ivMoveUp -> colorPickerView.offsetXY(0F, -3F)
            R.id.ivMoveDown -> colorPickerView.offsetXY(0F, 3F)
            R.id.ivMoveLeft -> colorPickerView.offsetXY(-3F, 0F)
            R.id.ivMoveRight -> colorPickerView.offsetXY(3F, 0F)
        }
    }
}