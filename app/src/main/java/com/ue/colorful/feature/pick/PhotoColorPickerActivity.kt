package com.ue.colorful.feature.pick

/*
 * Copyright (C) 2017 skydoves
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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