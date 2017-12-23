package com.ue.colorful.feature.coloring.impression

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.view.View
import com.ue.adapterdelegate.Item
import com.ue.colorful.R
import com.ue.colorful.event.SnackBarEvent
import com.ue.colorful.feature.main.BaseFragment
import com.ue.colorful.model.ImpressionItem
import com.ue.colorful.model.ImpressionTitle
import com.ue.colorful.util.SnackBarUtils
import kotlinx.android.synthetic.main.fragment_im_palette.view.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe


/**
 * Created by hawk on 2017/12/21.
 */
class IMPaletteFragment : BaseFragment(R.layout.fragment_im_palette, R.menu.menu_palette) {
    private lateinit var snackBar: Snackbar
    private var snackColor = Color.WHITE

    override fun initViews() {
        rootView.rvColorList.setHasFixedSize(true)
        rootView.rvColorList.adapter = ImpressionAdapter(activity as Activity, impressionItems)

        snackBar = Snackbar.make(rootView, "", Snackbar.LENGTH_LONG)
        SnackBarUtils.setView(snackBar, R.layout.layout_snack_bar)
        SnackBarUtils.setAction(snackBar, R.id.ivSnackPalette, View.OnClickListener { containerCallback?.addPaletteColor(snackColor) })
        SnackBarUtils.setAction(snackBar, R.id.ivSnackCopy, View.OnClickListener { containerCallback?.copyColor(snackColor) })
    }

    private val impressionItems: List<Item>
        get() {
            val items = ArrayList<Item>()

            val impressions = resources.getStringArray(R.array.impressions)
            val ims = resources.obtainTypedArray(R.array.im_all)
            for (a in impressions.indices) {
                items.add(ImpressionTitle(impressions[a]))

                val imTa = resources.obtainTypedArray(ims.getResourceId(a, -1))

                var i = 0
                while (i < imTa.length()) {
                    val color1 = resources.getIntArray(imTa.getResourceId(i, -1))
                    val color2Res = imTa.getResourceId(i + 1, -1)

                    if (color2Res == -1) items.add(ImpressionItem(color1, null))
                    else items.add(ImpressionItem(color1, resources.getIntArray(color2Res)))

                    i += 2
                }

                imTa.recycle()
            }
            ims.recycle()

            return items
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        EventBus.getDefault().register(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe
    fun onSnackBarEvent(event: SnackBarEvent) {
        snackColor = event.colorInt
        SnackBarUtils.setColor(snackBar, R.id.vSnackColor, snackColor)
        SnackBarUtils.setMessage(snackBar, R.id.tvSnackTxt, String.format("#%06X", 0xFFFFFF and snackColor))
        snackBar.show()
    }
}