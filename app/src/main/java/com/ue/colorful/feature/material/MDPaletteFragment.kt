package com.ue.colorful.feature.material

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.*
import com.ue.adapterdelegate.OnDelegateClickListener
import com.ue.colorful.R
import com.ue.colorful.constant.SPKeys
import com.ue.colorful.event.ShowPaletteEvent
import com.ue.colorful.model.PaletteColor
import com.ue.colorful.model.PaletteSection
import com.ue.colorful.util.SPUtils
import kotlinx.android.synthetic.main.fragment_mdpalette.*
import kotlinx.android.synthetic.main.fragment_mdpalette.view.*
import org.greenrobot.eventbus.EventBus

/**
 * Created by hawk on 2017/12/13.
 */
class MDPaletteFragment : Fragment() {
    private lateinit var paletteSections: List<PaletteSection>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_mdpalette, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val lastColorOption = SPUtils.getInt(SPKeys.LAST_MD_COLOR, 0)
        paletteSections = getPaletteSections(lastColorOption)

        view.rvColorList.setHasFixedSize(true)
        view.rvColorList.adapter = MDPaletteColorAdapter(activity, paletteSections[lastColorOption].paletteColors)

        view.rvColorOptions.setHasFixedSize(true)
        view.rvColorOptions.adapter = MDPaletteSectionAdapter(activity, paletteSections,
                OnDelegateClickListener { view, i ->
                    val adapter = rvColorList.adapter as MDPaletteColorAdapter
                    adapter.items.clear()
                    adapter.items.addAll(paletteSections[i].paletteColors)
                    adapter.notifyDataSetChanged()
                })
        //移动到指定位置，可以置顶则置顶
        (view.rvColorOptions.layoutManager as LinearLayoutManager).scrollToPositionWithOffset(lastColorOption, 0)
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_palette, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menuPalette -> {
                EventBus.getDefault().post(ShowPaletteEvent())
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}