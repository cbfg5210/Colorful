package com.ue.colorful.feature.pickphoto

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ue.colorful.R
import com.ue.colorful.event.ColorListener
import kotlinx.android.synthetic.main.fragment_photo_color_picker.*
import kotlinx.android.synthetic.main.fragment_photo_color_picker.view.*

class PhotoColorPickerFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_photo_color_picker, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.pcpPhotoColorPicker.setColorListener(object : ColorListener {
            override fun onColorSelected(color: Int) {
                view.tvHex.text = "#" + String.format("%06X", 0xFFFFFF and color)
                view.vColorEffect.setBackgroundColor(color)
            }
        })

        val directionListener = object : View.OnClickListener {
            override fun onClick(v: View) {
                when (v.id) {
                    R.id.ivThumbToggle -> {
                        view.ivThumbToggle.isSelected = !ivThumbToggle.isSelected
                        view.pcpPhotoColorPicker.toggleThumbColor()
                    }
                    R.id.ivMoveUp -> view.pcpPhotoColorPicker.offsetXY(0F, -3F)
                    R.id.ivMoveDown -> view.pcpPhotoColorPicker.offsetXY(0F, 3F)
                    R.id.ivMoveLeft -> view.pcpPhotoColorPicker.offsetXY(-3F, 0F)
                    R.id.ivMoveRight -> view.pcpPhotoColorPicker.offsetXY(3F, 0F)
                }
            }
        }
        view.ivThumbToggle.setOnClickListener(directionListener)
        view.ivMoveUp.setOnClickListener(directionListener)
        view.ivMoveDown.setOnClickListener(directionListener)
        view.ivMoveLeft.setOnClickListener(directionListener)
        view.ivMoveRight.setOnClickListener(directionListener)
    }
}