package com.ue.colorful.feature.pickpalette

import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.PixelFormat
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.app.AppCompatActivity
import android.widget.ImageView
import android.widget.TextView

import com.ue.colorful.R
import com.ue.colorful.widget.PaletteColorPickerView

import java.util.Locale

class PaletteColorPickerActivity : AppCompatActivity(), PaletteColorPickerView.OnColorChangedListener {

    private var colorPickerView: PaletteColorPickerView? = null
    private var newColorPanelView: ImageView? = null
    private var tvColorHex: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFormat(PixelFormat.RGBA_8888)

        setContentView(R.layout.activity_palette_color_picker)

        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        val initialColor = prefs.getInt("color_3", -0x1000000)

        colorPickerView = findViewById(R.id.cpv_color_picker_view)
        newColorPanelView = findViewById(R.id.cpv_color_panel_new)
        tvColorHex = findViewById(R.id.tvColorHex)

        colorPickerView!!.setOnColorChangedListener(this)
        colorPickerView!!.setColor(initialColor, true)

        onColorChanged(initialColor)
    }

    override fun onColorChanged(newColor: Int) {
        newColorPanelView!!.setBackgroundColor(newColor)
        tvColorHex!!.text = "#" + (if (Color.alpha(newColor) != 255) Integer.toHexString(newColor) else String.format("%06X", 0xFFFFFF and newColor)).toUpperCase(Locale.ENGLISH)
    }

    override fun onDestroy() {
        PreferenceManager.getDefaultSharedPreferences(this)
                .edit()
                .putInt("color_3", colorPickerView!!.color)
                .apply()
        super.onDestroy()
    }
}
