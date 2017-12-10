package com.ue.colorful.feature.material

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Color
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Toast
import com.ue.adapterdelegate.BaseAdapterDelegate
import com.ue.adapterdelegate.DelegationAdapter
import com.ue.adapterdelegate.OnDelegateClickListener
import com.ue.colorful.R
import com.ue.colorful.model.PaletteColor
import kotlinx.android.synthetic.main.item_palette_color.view.*

/**
 * Created by hawk on 2017/12/9.
 */
internal class PaletteColorAdapter(private val activity: Activity, items: List<PaletteColor>?) : DelegationAdapter<PaletteColor>(), OnDelegateClickListener {
    private val mClipboardManager: ClipboardManager by lazy {
        activity.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    }

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
        if (view.id == R.id.colorCopy) {
            val paletteColor = items[position]
            val clip = ClipData.newPlainText(activity.getString(R.string.color_clipboard, paletteColor.colorSectionName,
                    paletteColor.baseName), paletteColor.hexString)
            mClipboardManager.primaryClip = clip

            Toast.makeText(activity, activity.getString(R.string.color_copied, paletteColor.hexString), Toast.LENGTH_SHORT).show()
            return
        }
    }

    /**
     * Delegate
     */
    private class PaletteColorDelegate(private val activity: Activity) : BaseAdapterDelegate<PaletteColor>(activity, R.layout.item_palette_color) {

        override fun onCreateViewHolder(itemView: View): RecyclerView.ViewHolder {
            val holder = ViewHolder(itemView)
            holder.colorCopy.setOnClickListener { v -> onDelegateClickListener?.onClick(v, holder.adapterPosition) }
            return holder
        }

        override fun isForViewType(item: PaletteColor): Boolean {
            return true
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, paletteColor: PaletteColor, payloads: List<*>) {
            holder as ViewHolder
            holder.coloredZone.setBackgroundColor(paletteColor.hex)
            holder.colorTitle.text = paletteColor.baseName
            holder.colorContent.text = paletteColor.hexString
            if (isColorLight(paletteColor.hex)) {
                holder.colorCopy.setImageResource(R.mipmap.ic_content_copy_black_24dp)
                holder.colorTitle.setTextColor(ContextCompat.getColor(activity, R.color.color_card_title_dark_color))
                holder.colorContent.setTextColor(ContextCompat.getColor(activity, R.color.color_card_content_dark_color))
            } else {
                holder.colorCopy.setImageResource(R.mipmap.ic_content_copy_white_24dp)
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
            val colorCopy = itemView.colorCopy
            val coloredZone = itemView.coloredZone
        }
    }
}