package com.ue.colorful

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.widget.LinearLayout
import android.widget.TextView

class MultiColorPickerActivity : AppCompatActivity() {

    private var multiColorPickerView: MultiColorPickerView? = null

    private val selector0_colorListener = object : ColorListener {
        override fun onColorSelected(color: Int) {
            val textView = findViewById<TextView>(R.id.textView0)
            textView.text = "#" + multiColorPickerView!!.colorHtml

            val linearLayout = findViewById<LinearLayout>(R.id.linearLayout0)
            linearLayout.setBackgroundColor(color)
        }
    }

    private val selector1_colorListener = object : ColorListener {
        override fun onColorSelected(color: Int) {
            val textView = findViewById<TextView>(R.id.textView1)
            textView.text = "#" + multiColorPickerView!!.colorHtml

            val linearLayout = findViewById<LinearLayout>(R.id.linearLayout1)
            linearLayout.setBackgroundColor(color)
        }
    }

    private val selector2_colorListener = object : ColorListener {
        override fun onColorSelected(color: Int) {
            val textView = findViewById<TextView>(R.id.textView2)
            textView.text = "#" + multiColorPickerView!!.colorHtml

            val linearLayout = findViewById<LinearLayout>(R.id.linearLayout2)
            linearLayout.setBackgroundColor(color)
        }
    }

    private val selector3_colorListener = object : ColorListener {
        override fun onColorSelected(color: Int) {
            val textView = findViewById<TextView>(R.id.textView3)
            textView.text = "#" + multiColorPickerView!!.colorHtml

            val linearLayout = findViewById<LinearLayout>(R.id.linearLayout3)
            linearLayout.setBackgroundColor(color)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_multi_color_picker)

        multiColorPickerView = findViewById(R.id.multiColorPickerView)

        multiColorPickerView!!.setSelectorAlpha(0.6f)
        multiColorPickerView!!.addSelector(ContextCompat.getDrawable(this, R.drawable.wheel), selector0_colorListener)
        multiColorPickerView!!.addSelector(ContextCompat.getDrawable(this, R.drawable.wheel), selector1_colorListener)
        multiColorPickerView!!.addSelector(ContextCompat.getDrawable(this, R.drawable.wheel), selector2_colorListener)
        multiColorPickerView!!.addSelector(ContextCompat.getDrawable(this, R.drawable.wheel), selector3_colorListener)
    }
}
