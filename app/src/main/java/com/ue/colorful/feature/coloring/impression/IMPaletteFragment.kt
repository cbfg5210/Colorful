package com.ue.colorful.feature.coloring.impression

import com.ue.colorful.R
import com.ue.colorful.feature.main.BaseFragment
import com.ue.colorful.model.ImpressionItem
import kotlinx.android.synthetic.main.fragment_im_palette.view.*

/**
 * Created by hawk on 2017/12/21.
 */
class IMPaletteFragment : BaseFragment(R.layout.fragment_im_palette, R.menu.menu_palette) {
    override fun initViews() {
        rootView.rvColorList.setHasFixedSize(true)
        rootView.rvColorList.adapter = ImpressionAdapter(activity, impressionItems)
    }

    private val impressionItems: List<ImpressionItem>
        get() {
            val items = ArrayList<ImpressionItem>()
            var colors: ArrayList<IntArray>

            val impressions = resources.getStringArray(R.array.impressions)
            val ims = resources.obtainTypedArray(R.array.im_all)
            for (a in impressions.indices) {
                colors = ArrayList()

                val imTa = resources.obtainTypedArray(ims.getResourceId(a, -1))

                var i = 0
                while (i < imTa.length())
                    colors.add(resources.getIntArray(imTa.getResourceId(i++, -1)))

                imTa.recycle()
                items.add(ImpressionItem(impressions[a], colors))
            }
            ims.recycle()

            return items
        }
}