package com.ue.colorful.feature.material

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
import com.ue.colorful.constant.SPKeys
import com.ue.colorful.model.PaletteSection
import com.ue.colorful.util.SPUtils
import kotlinx.android.synthetic.main.item_palette_section.view.*

/**
 * Created by hawk on 2017/12/9.
 */
internal class MDPaletteSectionAdapter(private val activity: Activity, items: List<PaletteSection>?, private val onDelegateListener: OnDelegateClickListener?) : DelegationAdapter<PaletteSection>(), OnDelegateClickListener {
    private var selection: Int

    init {
        this.items = items ?: ArrayList()
        selection = SPUtils.getInt(SPKeys.LAST_MD_COLOR, 0)

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
        SPUtils.putInt(SPKeys.LAST_MD_COLOR, selection)
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