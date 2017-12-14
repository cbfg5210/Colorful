package com.ue.colorful.feature.pickpalette

import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ue.colorful.R
import com.ue.colorful.constant.SPKeys
import com.ue.colorful.util.SPUtils
import com.ue.colorful.widget.PaletteColorPickerView
import kotlinx.android.synthetic.main.fragment_palette_picker.*
import kotlinx.android.synthetic.main.fragment_palette_picker.view.*
import java.util.*

/**
 * Created by hawk on 2017/12/14.
 */
class PalettePickerFragment : Fragment(), PaletteColorPickerView.OnColorChangedListener {
    private lateinit var rootView: View;

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_palette_picker, container, false)
        return rootView
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val initialColor = SPUtils.getInt(SPKeys.LAST_PALETTE_COLOR, Color.BLACK)

        rootView.paletteColorPicker.setOnColorChangedListener(this)
        rootView.paletteColorPicker.setColor(initialColor, true)

        onColorChanged(initialColor)
    }

    override fun onColorChanged(newColor: Int) {
        rootView.ivColorEffect.setBackgroundColor(newColor)
        rootView.tvColorHex.text = "#" + (if (Color.alpha(newColor) != 255) Integer.toHexString(newColor) else String.format("%06X", 0xFFFFFF and newColor)).toUpperCase(Locale.ENGLISH)
    }

    override fun onDestroy() {
        SPUtils.putInt(SPKeys.LAST_PALETTE_COLOR, paletteColorPicker.color)
        super.onDestroy()
    }
}