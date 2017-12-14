package com.ue.colorful.feature.main

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.ue.colorful.R
import com.ue.colorful.event.RemovePaletteColorEvent
import kotlinx.android.synthetic.main.item_color_palette.view.*
import org.greenrobot.eventbus.EventBus

/**
 * Created by hawk on 2017/12/14.
 */
class ColorPaletteAdapter(private val activity: Activity, private val colors: ArrayList<Int>) : RecyclerView.Adapter<ColorPaletteAdapter.ViewHolder>() {
    private val mClipboardManager: ClipboardManager by lazy {
        activity.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_color_palette, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val color = colors[position]
        holder.vgColorPalette.setBackgroundColor(color)
        holder.tvHex.text = String.format("#%08X", color)

        holder.ivCopy.setOnClickListener { v ->
            val hex = holder.tvHex.text.toString()
            val clip = ClipData.newPlainText("copy", hex)
            mClipboardManager.primaryClip = clip

            Toast.makeText(activity, activity.getString(R.string.color_copied, hex), Toast.LENGTH_SHORT).show()
        }
        holder.ivRemove.setOnClickListener {
            EventBus.getDefault().post(RemovePaletteColorEvent(holder.adapterPosition))
            colors.removeAt(holder.adapterPosition)
            notifyItemRemoved(holder.adapterPosition)
        }
    }

    override fun getItemCount(): Int {
        return colors.size
    }

    class ViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val vgColorPalette = itemView.vgColorPalette
        val tvHex = itemView.tvHex
        val ivCopy = itemView.ivCopy
        val ivRemove = itemView.ivRemove
    }
}