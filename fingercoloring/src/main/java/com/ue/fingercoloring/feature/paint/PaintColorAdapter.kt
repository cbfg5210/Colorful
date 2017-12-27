package com.ue.fingercoloring.feature.paint

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.view.View
import com.ue.adapterdelegate.BaseAdapterDelegate
import com.ue.adapterdelegate.DelegationAdapter
import com.ue.adapterdelegate.OnDelegateClickListener
import com.ue.fingercoloring.R
import kotlinx.android.synthetic.main.item_paint_color.view.*

/**
 * Created by hawk on 2017/12/27.
 */
internal class PaintColorAdapter(activity: Activity, items: List<Int>) : DelegationAdapter<Int>(), OnDelegateClickListener {
    private var paintColorListener: OnDelegateClickListener? = null

    fun setColorSelectedListener(listener: OnDelegateClickListener) {
        this.paintColorListener = listener
    }

    init {
        this.items = items

        val delegate = PaintColorDelegate(activity)
        delegate.setOnDelegateClickListener(this)
        this.addDelegate(delegate)
    }

    override fun onClick(view: View, position: Int) {
        if (position < 0 || position >= itemCount) {
            return
        }
        paintColorListener?.onClick(view, items[position])
    }

    /**
     * Delegate
     */
    private class PaintColorDelegate(activity: Activity) : BaseAdapterDelegate<Int>(activity, R.layout.item_paint_color) {

        override fun onCreateViewHolder(itemView: View): RecyclerView.ViewHolder {
            return ViewHolder(itemView)
        }

        override fun isForViewType(item: Int): Boolean {
            return true
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Int, payloads: List<*>) {
            holder as ViewHolder
            holder.vPaintColor.setBackgroundColor(item)
        }

        private class ViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val vPaintColor = itemView.vPaintColor!!
        }
    }
}