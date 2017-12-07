package com.ue.colorful

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_multi_color_picker.*

class MultiColorPickerActivity : AppCompatActivity() {

    private val selector0_colorListener = object : ColorListener {
        override fun onColorSelected(color: Int) {
            textView0.text = "#" + multiColorPickerView!!.colorHtml
            linearLayout0.setBackgroundColor(color)
        }
    }

    private val selector1_colorListener = object : ColorListener {
        override fun onColorSelected(color: Int) {
            textView1.text = "#" + multiColorPickerView!!.colorHtml
            linearLayout1.setBackgroundColor(color)
        }
    }

    private val selector2_colorListener = object : ColorListener {
        override fun onColorSelected(color: Int) {
            textView2.text = "#" + multiColorPickerView!!.colorHtml
            linearLayout2.setBackgroundColor(color)
        }
    }

    private val selector3_colorListener = object : ColorListener {
        override fun onColorSelected(color: Int) {
            textView3.text = "#" + multiColorPickerView!!.colorHtml
            linearLayout3.setBackgroundColor(color)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_multi_color_picker)

        multiColorPickerView!!.setSelectorAlpha(0.6f)
        multiColorPickerView!!.addSelector(ContextCompat.getDrawable(this, R.drawable.wheel), selector0_colorListener)
        multiColorPickerView!!.addSelector(ContextCompat.getDrawable(this, R.drawable.wheel), selector1_colorListener)
        multiColorPickerView!!.addSelector(ContextCompat.getDrawable(this, R.drawable.wheel), selector2_colorListener)
        multiColorPickerView!!.addSelector(ContextCompat.getDrawable(this, R.drawable.wheel), selector3_colorListener)
    }
}
