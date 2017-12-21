package com.ue.colorful.feature.coloring.impression

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.view.View
import com.ue.adapterdelegate.BaseAdapterDelegate
import com.ue.adapterdelegate.DelegationAdapter
import com.ue.adapterdelegate.OnDelegateClickListener
import com.ue.colorful.R
import kotlinx.android.synthetic.main.item_sub_impression.view.*

/**
 * Created by hawk on 2017/12/21.
 */
class ImpressionSubAdapter(activity: Activity, items: List<IntArray>) : DelegationAdapter<IntArray>(), OnDelegateClickListener {

    init {
        this.items = ArrayList<IntArray>()
        if (items != null) this.items.addAll(items)

        this.addDelegate(ImpressionItemDelegate(activity))
    }

    override fun onClick(view: View, position: Int) {
        if (position < 0 || position >= itemCount) {
            return
        }
    }

    /**
     * Delegate
     */
    private class ImpressionItemDelegate(activity: Activity) : BaseAdapterDelegate<IntArray>(activity, R.layout.item_sub_impression) {

        override fun onCreateViewHolder(itemView: View): RecyclerView.ViewHolder {
            return ViewHolder(itemView)
        }

        override fun isForViewType(item: IntArray): Boolean {
            return true
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: IntArray, payloads: List<*>) {
            holder as ViewHolder

            holder.color1.setBackgroundColor(item[0])
            holder.color2.setBackgroundColor(item[1])
            holder.color3.setBackgroundColor(item[2])

        }

        class ViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val color1 = itemView.ivColor1!!
            val color2 = itemView.ivColor2!!
            val color3 = itemView.ivColor3!!
        }
    }
}