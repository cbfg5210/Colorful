package com.ue.colorful.feature.coloring.md

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ue.adapterdelegate.OnDelegateClickListener
import com.ue.colorful.R
import com.ue.colorful.constant.SPKeys
import com.ue.colorful.feature.coloring.md.MDPaletteColorAdapter.MDPaletteListener
import com.ue.colorful.feature.picker.BasePickerFragment
import com.ue.colorful.model.PaletteColor
import com.ue.colorful.model.PaletteSection
import com.ue.colorful.util.SPUtils
import kotlinx.android.synthetic.main.fragment_mdpalette.*
import kotlinx.android.synthetic.main.fragment_mdpalette.view.*

/**
 * Created by hawk on 2017/12/13.
 */
class MDPaletteFragment : BasePickerFragment(R.layout.fragment_mdpalette, R.menu.menu_palette) {
    private lateinit var paletteSections: List<PaletteSection>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        val lastColorOption = SPUtils.getInt(SPKeys.LAST_MD_COLOR, 0)
        paletteSections = getPaletteSections(lastColorOption)

        rootView.rvColorList.setHasFixedSize(true)
        rootView.rvColorList.adapter = MDPaletteColorAdapter(activity, paletteSections[lastColorOption].paletteColors,
                object : MDPaletteListener {
                    override fun copyColor(color: Int) {
                        containerCallbck?.copyColor(color)
                    }

                    override fun addPaletteColor(color: Int) {
                        containerCallbck?.addPaletteColor(color)
                    }

                })

        rootView.rvColorOptions.setHasFixedSize(true)
        rootView.rvColorOptions.adapter = MDPaletteSectionAdapter(activity, paletteSections,
                OnDelegateClickListener { view, i ->
                    val adapter = rvColorList.adapter as MDPaletteColorAdapter
                    adapter.items.clear()
                    adapter.items.addAll(paletteSections[i].paletteColors)
                    adapter.notifyDataSetChanged()
                })
        //移动到指定位置，可以置顶则置顶
        (rootView.rvColorOptions.layoutManager as LinearLayoutManager).scrollToPositionWithOffset(lastColorOption, 0)

        return rootView
    }

    private fun getPaletteSections(lastColorOption: Int): List<PaletteSection> {
        val colorSectionsNames = resources.getStringArray(R.array.color_sections_names)
        val colorSectionsValues = resources.getIntArray(R.array.color_sections_colors)

        val baseColorNamesArray = resources.obtainTypedArray(R.array.all_color_names)
        val colorValuesArray = resources.obtainTypedArray(R.array.all_color_list)

        val paletteSections = ArrayList<PaletteSection>()
        var i = 0
        while (i < colorSectionsNames.size) {
            val paletteColors = java.util.ArrayList<PaletteColor>()
            val paletteSectionNames = resources.getStringArray(baseColorNamesArray.getResourceId(i, -1))
            val paletteColorValues = resources.getIntArray(colorValuesArray.getResourceId(i, -1))

            var j = 0
            while (j < paletteSectionNames.size) {
                paletteColors.add(PaletteColor(colorSectionsNames[i], paletteColorValues[j], paletteSectionNames[j]))
                j++
            }
            paletteSections.add(PaletteSection(colorSectionsNames[i], colorSectionsValues[i], paletteColors))
            i++
        }
        paletteSections[lastColorOption].isSelected = true

        return paletteSections
    }
}