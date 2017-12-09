package com.ue.colorful.feature.main

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.ue.adapterdelegate.OnDelegateClickListener
import com.ue.colorful.R
import com.ue.colorful.constant.SPKeys
import com.ue.colorful.feature.material.PaletteColorAdapter
import com.ue.colorful.model.PaletteColor
import com.ue.colorful.model.PaletteSection
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedPreferences = getSharedPreferences(SPKeys.SP_NAME, Context.MODE_PRIVATE)
        val lastColorOption = sharedPreferences.getInt(SPKeys.LAST_COLOR_OPTION, 0)
        paletteSections[lastColorOption].isSelected = true

        rvColorOptions.adapter = PaletteSectionAdapter(this, paletteSections,
                OnDelegateClickListener { view, i ->
                    val adapter = rvColorList.adapter as PaletteColorAdapter
                    adapter.items.clear()
                    adapter.items.addAll(paletteSections[i].paletteColorList)
                    adapter.notifyDataSetChanged()

                    sharedPreferences.edit()
                            .putInt(SPKeys.LAST_COLOR_OPTION, i)
                            .apply()
                })

        rvColorList.adapter = PaletteColorAdapter(this, paletteSections[lastColorOption].paletteColorList)
    }

    private val paletteSections: MutableList<PaletteSection>
        get() {
            val colorSectionsNames = resources.getStringArray(R.array.color_sections_names)
            val colorSectionsValues = resources.getIntArray(R.array.color_sections_colors)

            val baseColorNamesArray = resources.obtainTypedArray(R.array.all_color_names)
            val colorValuesArray = resources.obtainTypedArray(R.array.all_color_list)

            var mColorList = ArrayList<PaletteSection>()
            var i = 0
            while (i < colorSectionsNames.size) {
                val paletteColorList = ArrayList<PaletteColor>()
                val baseColorNames = resources.getStringArray(baseColorNamesArray.getResourceId(i, -1))
                val colorValues = resources.getIntArray(colorValuesArray.getResourceId(i, -1))

                var j = 0
                while (j < baseColorNames.size) {
                    paletteColorList.add(PaletteColor(colorSectionsNames[i], colorValues[j], baseColorNames[j]))
                    j++
                }
                mColorList!!.add(PaletteSection(colorSectionsNames[i], colorSectionsValues[i], paletteColorList))
                i++
            }
            return mColorList
        }
}
