package com.ue.colorful.feature.picker.palette

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ue.colorful.R
import com.ue.colorful.constant.SPKeys
import com.ue.colorful.feature.picker.BasePickerFragment
import com.ue.colorful.util.SPUtils
import com.ue.colorful.widget.PaletteColorPickerView
import kotlinx.android.synthetic.main.fragment_palette_picker.view.*
import kotlinx.android.synthetic.main.layout_common_picker.view.*

/**
 * Created by hawk on 2017/12/14.
 */
class PalettePickerFragment : BasePickerFragment(R.layout.fragment_palette_picker), PaletteColorPickerView.OnColorChangedListener {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        val initialColor = SPUtils.getInt(SPKeys.LAST_PALETTE_COLOR, Color.BLACK)

        rootView.paletteColorPicker.setOnColorChangedListener(this)
        rootView.paletteColorPicker.setColor(initialColor, true)

        rootView.ivAddColor.setOnClickListener(pickerListener)
        rootView.ivCopy.setOnClickListener(pickerListener)

        onColorChanged(initialColor)

        return rootView
    }

    override fun getColorInt(): Int {
        return rootView.paletteColorPicker.color
    }

    override fun onColorChanged(newColor: Int) {
        rootView.ivColorEffect.setBackgroundColor(newColor)
        rootView.tvColorHex.text = String.format("#%08X", newColor)
    }

    override fun onDestroy() {
        SPUtils.putInt(SPKeys.LAST_PALETTE_COLOR, rootView.paletteColorPicker.color)
        super.onDestroy()
    }
}