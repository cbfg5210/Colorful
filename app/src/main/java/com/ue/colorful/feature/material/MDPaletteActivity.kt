package com.ue.colorful.feature.material

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.ue.adapterdelegate.OnDelegateClickListener
import com.ue.colorful.R
import com.ue.colorful.constant.Constants
import com.ue.colorful.model.PaletteColor
import com.ue.colorful.model.PaletteSection
import com.ue.colorful.util.SPUtils
import kotlinx.android.synthetic.main.activity_mdpalette.*

class MDPaletteActivity : AppCompatActivity() {
    private lateinit var paletteSections: List<PaletteSection>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mdpalette)

        val lastColorOption = SPUtils.getInt(Constants.LAST_MD_COLOR, 0)
        paletteSections = getPaletteSections(lastColorOption)

        rvColorList.setHasFixedSize(true)
        rvColorList.adapter = PaletteColorAdapter(this, paletteSections[lastColorOption].paletteColors)

        rvColorOptions.setHasFixedSize(true)
        rvColorOptions.adapter = PaletteSectionAdapter(this, paletteSections,
                OnDelegateClickListener { view, i ->
                    val adapter = rvColorList.adapter as PaletteColorAdapter
                    adapter.items.clear()
                    adapter.items.addAll(paletteSections[i].paletteColors)
                    adapter.notifyDataSetChanged()
                })
        //移动到指定位置，可以置顶则置顶
        (rvColorOptions.layoutManager as LinearLayoutManager).scrollToPositionWithOffset(lastColorOption, 0)
    }

    private fun getPaletteSections(lastColorOption: Int): List<PaletteSection> {
        val colorSectionsNames = resources.getStringArray(R.array.color_sections_names)
        val colorSectionsValues = resources.getIntArray(R.array.color_sections_colors)

        val baseColorNamesArray = resources.obtainTypedArray(R.array.all_color_names)
        val colorValuesArray = resources.obtainTypedArray(R.array.all_color_list)

        val paletteSections = java.util.ArrayList<PaletteSection>()
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
