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

        photoColorPicker.setColorListener(object : ColorListener {
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
                photoColorPicker.toggleThumbColor()
            }
            R.id.ivMoveUp -> photoColorPicker.offsetXY(0F, -3F)
            R.id.ivMoveDown -> photoColorPicker.offsetXY(0F, 3F)
            R.id.ivMoveLeft -> photoColorPicker.offsetXY(-3F, 0F)
            R.id.ivMoveRight -> photoColorPicker.offsetXY(3F, 0F)
        }
    }
}