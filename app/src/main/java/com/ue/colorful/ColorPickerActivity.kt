package com.ue.colorful

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
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView

class ColorPickerActivity : AppCompatActivity() {

    private var colorPickerView: ColorPickerView? = null

    private var FLAG_PALETTE = false
    private var FLAG_SELECTOR = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_color_picker)

        colorPickerView = findViewById(R.id.colorPickerView)
        colorPickerView!!.setColorListener(object : ColorListener {
            override fun onColorSelected(color: Int) {
                setLayoutColor(color)
            }
        })
    }

    /**
     * set layout color & textView html code
     * @param color
     */
    private fun setLayoutColor(color: Int) {
        val textView = findViewById<TextView>(R.id.textView)
        textView.text = "#" + colorPickerView!!.colorHtml

        val linearLayout = findViewById<LinearLayout>(R.id.linearLayout)
        linearLayout.setBackgroundColor(color)
    }

    /**
     * change palette drawable resource
     * you must initialize at first in xml
     * @param v
     */
    fun palette(v: View) {
        if (FLAG_PALETTE)
            colorPickerView!!.setPaletteDrawable(ContextCompat.getDrawable(this, R.drawable.palette))
        else
            colorPickerView!!.setPaletteDrawable(ContextCompat.getDrawable(this, R.drawable.palettebar))
        FLAG_PALETTE = !FLAG_PALETTE
    }

    /**
     * change selector drawable resource
     * you must initialize at first in xml
     * @param v
     */
    fun selector(v: View) {
        if (FLAG_SELECTOR)
            colorPickerView!!.setSelectorDrawable(ContextCompat.getDrawable(this, R.drawable.wheel))
        else
            colorPickerView!!.setSelectorDrawable(ContextCompat.getDrawable(this, R.drawable.wheel_dark))
        FLAG_SELECTOR = !FLAG_SELECTOR
    }

    /**
     * moving selector's points (x, y)
     * @param v
     */
    fun points(v: View) {
        val x = (Math.random() * 600).toInt() + 100
        val y = (Math.random() * 400).toInt() + 150
        colorPickerView!!.setSelectorPoint(x, y)
    }
}