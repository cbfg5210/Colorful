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
import com.ue.colorful.constant.SPKeys
import com.ue.colorful.event.ClearColorEvent
import com.ue.colorful.event.RemoveColorEvent
import com.ue.colorful.util.SPUtils
import kotlinx.android.synthetic.main.item_color_palette.view.*
import org.greenrobot.eventbus.EventBus

/**
 * Created by hawk on 2017/12/14.
 */
class ColorPaletteAdapter(private val activity: Activity, private val colors: ArrayList<Int>) : RecyclerView.Adapter<ColorPaletteAdapter.ViewHolder>() {
    private var lastSelection = 0
    private var selection = 0

    private val mClipboardManager: ClipboardManager by lazy {
        activity.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    }

    fun removeSelection() {
        colors.removeAt(selection)
        notifyItemRemoved(selection)
        selection = 0
        lastSelection = 0
        notifyItemChanged(0)

        SPUtils.putString(SPKeys.PALETTE_COLORS, colors.toString())
        EventBus.getDefault().post(RemoveColorEvent(selection))
    }

    fun copyColor() {
        val hex = String.format("#%08X", colors[selection])
        val clip = ClipData.newPlainText("copy", hex)
        mClipboardManager.primaryClip = clip

        Toast.makeText(activity, activity.getString(R.string.color_copied, hex), Toast.LENGTH_SHORT).show()
    }

    fun clear() {
        lastSelection = 0
        selection = 0
        colors.clear()
        notifyDataSetChanged()

        SPUtils.putString(SPKeys.PALETTE_COLORS, "")
        EventBus.getDefault().post(ClearColorEvent())
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_color_palette, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val color = colors[position]
        holder.tvHex.setBackgroundColor(color)
        holder.tvHex.text = String.format("#%08X", color)

        holder.ivState.visibility = if (selection == position) View.VISIBLE else View.INVISIBLE

        holder.vgColorPalette.setOnClickListener({ _ ->
            lastSelection = selection
            selection = holder.adapterPosition
            notifyItemChanged(lastSelection)
            notifyItemChanged(selection)
        })
    }

    override fun getItemCount(): Int {
        return colors.size
    }

    class ViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val vgColorPalette = itemView.vgColorPalette
        val tvHex = itemView.tvHex
        val ivState = itemView.ivState
    }
}