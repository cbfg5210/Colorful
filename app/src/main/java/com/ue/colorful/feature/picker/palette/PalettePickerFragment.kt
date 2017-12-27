package com.ue.colorful.feature.picker.palette

import android.graphics.Color
import com.ue.colorful.R
import com.ue.colorful.constant.SPKeys
import com.ue.colorful.feature.main.BaseFragment
import com.ue.colorful.widget.PaletteColorPickerView
import com.ue.fingercoloring.util.SPUtils
import kotlinx.android.synthetic.main.fragment_palette_picker.view.*
import kotlinx.android.synthetic.main.layout_common_picker.view.*

/**
 * Created by hawk on 2017/12/14.
 */
class PalettePickerFragment : BaseFragment(R.layout.fragment_palette_picker,R.menu.menu_palette), PaletteColorPickerView.OnColorChangedListener {
    override fun initViews() {
        val initialColor = SPUtils.getInt(SPKeys.LAST_PALETTE_COLOR, Color.BLACK)

        rootView.paletteColorPicker.setOnColorChangedListener(this)
        rootView.paletteColorPicker.setColor(initialColor, true)

        rootView.ivAddColor.setOnClickListener({ containerCallback?.addPaletteColor(rootView.paletteColorPicker.color) })
        rootView.ivCopy.setOnClickListener({ containerCallback?.copyColor(rootView.paletteColorPicker.color) })

        onColorChanged(initialColor)
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