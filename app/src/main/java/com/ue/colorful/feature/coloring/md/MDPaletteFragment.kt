package com.ue.colorful.feature.coloring.md

import android.support.v7.widget.LinearLayoutManager
import com.ue.adapterdelegate.OnDelegateClickListener
import com.ue.colorful.R
import com.ue.colorful.constant.SPKeys
import com.ue.colorful.feature.coloring.md.MDPaletteColorAdapter.MDPaletteListener
import com.ue.colorful.feature.main.BaseFragment
import com.ue.colorful.model.MDColor
import com.ue.colorful.model.PaletteSection
import com.ue.colorful.util.SPUtils
import kotlinx.android.synthetic.main.fragment_mdpalette.*
import kotlinx.android.synthetic.main.fragment_mdpalette.view.*

/**
 * Created by hawk on 2017/12/13.
 */
class MDPaletteFragment : BaseFragment(R.layout.fragment_mdpalette, R.menu.menu_palette) {
    private lateinit var paletteSections: List<PaletteSection>

    override fun initViews() {
        val lastSelection = SPUtils.getInt(SPKeys.LAST_MD_COLOR, 0)
        paletteSections = getPaletteSections(lastSelection)

        rootView.rvColorList.setHasFixedSize(true)
        rootView.rvColorList.adapter = MDPaletteColorAdapter(activity, paletteSections[lastSelection].paletteColors,
                object : MDPaletteListener {
                    override fun copyColor(color: Int) {
                        containerCallback?.copyColor(color)
                    }

                    override fun addPaletteColor(color: Int) {
                        containerCallback?.addPaletteColor(color)
                    }
                })

        rootView.rvColorOptions.setHasFixedSize(true)
        rootView.rvColorOptions.adapter = MDPaletteSectionAdapter(activity, paletteSections,
                OnDelegateClickListener { _, i ->
                    val adapter = rvColorList.adapter as MDPaletteColorAdapter
                    adapter.items.clear()
                    adapter.items.addAll(paletteSections[i].paletteColors)
                    adapter.notifyDataSetChanged()
                })
        //移动到指定位置，可以置顶则置顶
        (rootView.rvColorOptions.layoutManager as LinearLayoutManager).scrollToPositionWithOffset(lastSelection, 0)
    }

    private fun getPaletteSections(lastColorOption: Int): List<PaletteSection> {
        val colorSectionsNames = resources.getStringArray(R.array.color_sections_names)
        val colorSectionsValues = resources.getIntArray(R.array.color_sections_colors)

        val baseColorNamesArray = resources.obtainTypedArray(R.array.all_color_names)
        val colorValuesArray = resources.obtainTypedArray(R.array.all_color_list)

        val paletteSections = ArrayList<PaletteSection>()
        var i = 0
        while (i < colorSectionsNames.size) {
            val paletteColors = ArrayList<MDColor>()
            val paletteSectionNames = resources.getStringArray(baseColorNamesArray.getResourceId(i, -1))
            val paletteColorValues = resources.getIntArray(colorValuesArray.getResourceId(i, -1))

            var j = 0
            while (j < paletteSectionNames.size) {
                paletteColors.add(MDColor(paletteColorValues[j], paletteSectionNames[j]))
                j++
            }
            paletteSections.add(PaletteSection(colorSectionsNames[i], colorSectionsValues[i], paletteColors))
            i++
        }
        paletteSections[lastColorOption].isSelected = true

        return paletteSections
    }
}