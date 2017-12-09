package com.ue.colorful.feature.main

import android.app.Activity
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.StateListDrawable
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.ue.adapterdelegate.BaseAdapterDelegate
import com.ue.adapterdelegate.DelegationAdapter
import com.ue.adapterdelegate.OnDelegateClickListener
import com.ue.colorful.R
import com.ue.colorful.model.PaletteSection
import kotlinx.android.synthetic.main.item_palette_section.view.*

/**
 * Created by hawk on 2017/12/9.
 */
internal class PaletteSectionAdapter(private val activity: Activity, items: List<PaletteSection>?, private val onDelegateListener: OnDelegateClickListener?) : DelegationAdapter<PaletteSection>(), OnDelegateClickListener {
    private var selection: Int = 0

    init {
        this.items = items ?: ArrayList()

        val delegate = PaletteColorSectionDelegate(activity)
        delegate.setOnDelegateClickListener(this)
        this.addDelegate(delegate)
    }

    override fun onClick(view: View, position: Int) {
        if (position < 0 || position >= itemCount) {
            return
        }
        if (selection == position) return

        items.get(selection).isSelected = false
        items.get(position).isSelected = true
        notifyItemChanged(selection)
        notifyItemChanged(position)

        selection = position
        onDelegateListener?.onClick(view, position)
    }

    /**
     * Delegate
     */
    private class PaletteColorSectionDelegate(activity: Activity) : BaseAdapterDelegate<PaletteSection>(activity, R.layout.item_palette_section) {

        override fun onCreateViewHolder(itemView: View): RecyclerView.ViewHolder {
            return ViewHolder(itemView as TextView)
        }

        override fun isForViewType(item: PaletteSection): Boolean {
            return true
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, paletteSection: PaletteSection, payloads: List<*>) {
            holder as ViewHolder
            holder.setPaletteSection(paletteSection)
        }
    }

    private class ViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ctvColorName = itemView.ctvColorName

        fun setPaletteSection(paletteSection: PaletteSection) {
            ctvColorName.text = paletteSection.colorSectionName
            val stateDrawable = StateListDrawable()
            stateDrawable.addState(intArrayOf(android.R.attr.state_selected), ColorDrawable(paletteSection.colorSectionValue))
            ctvColorName.setBackgroundDrawable(stateDrawable)
            ctvColorName.isSelected = paletteSection.isSelected
        }
    }
}