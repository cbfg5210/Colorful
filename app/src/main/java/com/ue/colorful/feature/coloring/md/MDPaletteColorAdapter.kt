package com.ue.colorful.feature.coloring.md

import android.app.Activity
import android.graphics.Color
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.View
import com.ue.adapterdelegate.BaseAdapterDelegate
import com.ue.adapterdelegate.DelegationAdapter
import com.ue.adapterdelegate.OnDelegateClickListener
import com.ue.colorful.R
import com.ue.colorful.model.PaletteColor
import kotlinx.android.synthetic.main.item_md_palette_color.view.*

/**
 * Created by hawk on 2017/12/9.
 */
internal class MDPaletteColorAdapter(private val activity: Activity, items: List<PaletteColor>?, private val mdPaletteListener: MDPaletteListener) : DelegationAdapter<PaletteColor>(), OnDelegateClickListener {

    init {
        this.items = items ?: ArrayList()

        val delegate = PaletteColorDelegate(activity)
        delegate.setOnDelegateClickListener(this)
        this.addDelegate(delegate)
    }

    override fun onClick(view: View, position: Int) {
        if (position < 0 || position >= itemCount) {
            return
        }
        when (view.id) {
            R.id.ivCopy -> mdPaletteListener?.copyColor(items[position].hex)
            R.id.ivAddColor -> mdPaletteListener?.addPaletteColor(items[position].hex)
        }
    }

    /**
     * Delegate
     */
    private class PaletteColorDelegate(private val activity: Activity) : BaseAdapterDelegate<PaletteColor>(activity, R.layout.item_md_palette_color) {

        override fun onCreateViewHolder(itemView: View): RecyclerView.ViewHolder {
            val holder = ViewHolder(itemView)
            val clickListener = object : View.OnClickListener {
                override fun onClick(v: View?) {
                    onDelegateClickListener?.onClick(v, holder.adapterPosition)
                }
            }
            holder.ivCopy.setOnClickListener(clickListener)
            holder.ivAddColor.setOnClickListener(clickListener)
            return holder
        }

        override fun isForViewType(item: PaletteColor): Boolean {
            return true
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, paletteColor: PaletteColor, payloads: List<*>) {
            holder as ViewHolder
            holder.coloredZone.setBackgroundColor(paletteColor.hex)
            holder.colorTitle.text = paletteColor.baseName
            holder.colorContent.text = paletteColor.hexString.toUpperCase()
            if (isColorLight(paletteColor.hex)) {
                holder.colorTitle.setTextColor(ContextCompat.getColor(activity, R.color.color_card_title_dark_color))
                holder.colorContent.setTextColor(ContextCompat.getColor(activity, R.color.color_card_content_dark_color))
            } else {
                holder.colorTitle.setTextColor(ContextCompat.getColor(activity, R.color.color_card_title_light_color))
                holder.colorContent.setTextColor(ContextCompat.getColor(activity, R.color.color_card_content_light_color))
            }
        }

        private fun isColorLight(hex: Int): Boolean {
            val hsb = FloatArray(3)
            Color.colorToHSV(hex, hsb)
            return hsb[2] > 0.5
        }

        private class ViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val colorTitle = itemView.colorTitle
            val colorContent = itemView.colorContent
            val ivCopy = itemView.ivCopy
            val coloredZone = itemView.vgColorZone
            val ivAddColor = itemView.ivAddColor
        }
    }

    interface MDPaletteListener {
        fun copyColor(color: Int)
        fun addPaletteColor(color: Int)
    }
}