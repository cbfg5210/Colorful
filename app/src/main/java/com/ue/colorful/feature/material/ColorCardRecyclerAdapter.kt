package com.ue.colorful.feature.material

import android.content.Context
import android.graphics.Color
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ue.colorful.R
import com.ue.colorful.model.PaletteColor
import kotlinx.android.synthetic.main.card_color.view.*
import kotlinx.android.synthetic.main.iv_color_copy.view.*

/**
 * Created by gimbert on 14-11-10.
 */
class ColorCardRecyclerAdapter(private val mContext: Context, private val mCards: List<PaletteColor>, private val mCallback: ColorCardRecyclerAdapterCallback?) : RecyclerView.Adapter<ColorCardRecyclerAdapter.ViewHolder>() {
    private var hsb: FloatArray? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val rootView = LayoutInflater.from(mContext).inflate(R.layout.card_color, parent, false)
        return ViewHolder(rootView)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val paletteColor = mCards[position]
        viewHolder.coloredZone.setBackgroundColor(paletteColor.hex)
        viewHolder.colorTitle.text = paletteColor.baseName
        viewHolder.colorContent.text = paletteColor.hexString
        if (isColorLight(paletteColor.hex)) {
            viewHolder.colorCopy.setImageResource(R.mipmap.ic_content_copy_black_24dp)
            viewHolder.colorTitle.setTextColor(ContextCompat.getColor(mContext, R.color.color_card_title_dark_color))
            viewHolder.colorContent.setTextColor(ContextCompat.getColor(mContext, R.color.color_card_content_dark_color))
        } else {
            viewHolder.colorCopy.setImageResource(R.mipmap.ic_content_copy_white_24dp)
            viewHolder.colorTitle.setTextColor(ContextCompat.getColor(mContext, R.color.color_card_title_light_color))
            viewHolder.colorContent.setTextColor(ContextCompat.getColor(mContext, R.color.color_card_content_light_color))
        }
        viewHolder.colorCopy.setOnClickListener {
            mCallback?.onCopyColorClicked(paletteColor)
        }
    }

    override fun getItemCount(): Int {
        return mCards.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val coloredZone = itemView.coloredZone
        val colorTitle = itemView.colorTitle
        val colorContent = itemView.colorContent
        val colorCopy = itemView.colorCopy
    }

    private fun isColorLight(hex: Int): Boolean {
        hsb = FloatArray(3)
        Color.colorToHSV(hex, hsb)

        return hsb!![2] > 0.5
    }

    interface ColorCardRecyclerAdapterCallback {
        fun onCopyColorClicked(paletteColor: PaletteColor)
    }
}