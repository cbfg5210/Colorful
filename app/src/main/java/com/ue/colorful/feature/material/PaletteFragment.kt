package com.ue.colorful.feature.material

import android.app.Fragment
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.*
import android.widget.Toast
import com.ue.colorful.R
import com.ue.colorful.model.PaletteColor
import com.ue.colorful.model.PaletteColorSection
import kotlinx.android.synthetic.main.fragment_color_palette.*

class PaletteFragment : Fragment(), ColorCardRecyclerAdapter.ColorCardRecyclerAdapterCallback {
    private var mRecyclerAdapter: ColorCardRecyclerAdapter? = null
    private var mPaletteColorSection: PaletteColorSection? = null

    protected var mClipboardManager: ClipboardManager? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_color_palette, container, false)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.palette_color_menu, menu)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState != null) {
            mPaletteColorSection = savedInstanceState.getParcelable(SECTION_KEY)
        }
        if (mPaletteColorSection == null) {
            mPaletteColorSection = arguments.getParcelable(ARG_COLOR_SECTION)
        }

        mRecyclerAdapter = ColorCardRecyclerAdapter(activity, mPaletteColorSection!!.paletteColorList, this)
        paletteCardList.layoutManager = LinearLayoutManager(activity)
        paletteCardList.adapter = mRecyclerAdapter
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelable(SECTION_KEY, mPaletteColorSection)
        super.onSaveInstanceState(outState)
    }

    fun replaceColorCardList(paletteColorSection: PaletteColorSection) {
        mPaletteColorSection = paletteColorSection
        mRecyclerAdapter = ColorCardRecyclerAdapter(activity,
                mPaletteColorSection!!.paletteColorList, this)
        paletteCardList?.adapter = mRecyclerAdapter
    }

    fun scrollToTop() {
        paletteCardList?.smoothScrollToPosition(0)
    }

    override fun onCopyColorClicked(paletteColor: PaletteColor) {
        if (mClipboardManager == null) {
            mClipboardManager = activity.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        }
        val clip = ClipData.newPlainText(getString(R.string.color_clipboard, paletteColor.colorSectionName,
                paletteColor.baseName), paletteColor.hexString)
        mClipboardManager!!.primaryClip = clip

        Toast.makeText(activity, getString(R.string.color_copied, paletteColor.hexString),
                Toast.LENGTH_SHORT).show()
    }

    companion object {

        val ARG_COLOR_SECTION = "COLOR_SECTION"

        private val SECTION_KEY = "SECTION_KEY"
    }
}
