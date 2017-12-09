package com.ue.colorful.feature.material

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.StateListDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.ue.colorful.R
import com.ue.colorful.model.PaletteColorSection

class DrawerAdapter(private val mContext: Context, private val mColorList: List<PaletteColorSection>?) : BaseAdapter() {

    override fun getCount(): Int {
        return mColorList?.size ?: 0
    }

    override fun getItem(position: Int): Any {
        return mColorList!![position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        val holder: ViewHolder
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.nav_item, parent, false)
            holder = ViewHolder(convertView as TextView)
            convertView!!.tag = holder
        } else {
            holder = convertView.tag as ViewHolder
        }

        val paletteColorSection = mColorList!![position]
        val colorName = paletteColorSection.colorSectionName
        holder.textView.text = colorName

        val sld = StateListDrawable()
        val d = ColorDrawable(paletteColorSection.colorSectionValue)
        sld.addState(intArrayOf(android.R.attr.state_pressed), d)
        sld.addState(intArrayOf(android.R.attr.state_checked), d)
        holder.textView.setBackgroundDrawable(sld)
        return convertView
    }

    private class ViewHolder constructor(val textView: TextView)
}
