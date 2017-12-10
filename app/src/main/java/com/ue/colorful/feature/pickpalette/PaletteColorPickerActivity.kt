package com.ue.colorful.feature.pickpalette

import android.graphics.Color
import android.graphics.PixelFormat
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.ue.colorful.R
import com.ue.colorful.constant.SPKeys
import com.ue.colorful.util.SPUtils
import com.ue.colorful.widget.PaletteColorPickerView
import kotlinx.android.synthetic.main.activity_palette_color_picker.*
import java.util.*

class PaletteColorPickerActivity : AppCompatActivity(), PaletteColorPickerView.OnColorChangedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFormat(PixelFormat.RGBA_8888)
        setContentView(R.layout.activity_palette_color_picker)

        val initialColor = SPUtils.getInt(SPKeys.LAST_PALETTE_COLOR, Color.BLACK)

        paletteColorPicker.setOnColorChangedListener(this)
        paletteColorPicker.setColor(initialColor, true)

        onColorChanged(initialColor)
    }

    override fun onColorChanged(newColor: Int) {
        ivColorEffect.setBackgroundColor(newColor)
        tvColorHex.text = "#" + (if (Color.alpha(newColor) != 255) Integer.toHexString(newColor) else String.format("%06X", 0xFFFFFF and newColor)).toUpperCase(Locale.ENGLISH)
    }

    override fun onDestroy() {
        SPUtils.putInt(SPKeys.LAST_PALETTE_COLOR, paletteColorPicker.color)
        super.onDestroy()
    }
}
