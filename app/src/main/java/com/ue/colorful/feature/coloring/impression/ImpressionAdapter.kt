package com.ue.colorful.feature.coloring.impression

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.view.View
import com.ue.adapterdelegate.BaseAdapterDelegate
import com.ue.adapterdelegate.DelegationAdapter
import com.ue.adapterdelegate.OnDelegateClickListener
import com.ue.colorful.R
import com.ue.colorful.model.ImpressionItem
import kotlinx.android.synthetic.main.item_impression.view.*

/**
 * Created by hawk on 2017/12/21.
 */
class ImpressionAdapter(activity: Activity, items: List<ImpressionItem>) : DelegationAdapter<ImpressionItem>(), OnDelegateClickListener {

    init {
        this.items = items

        this.addDelegate(ImpressionItemDelegate(activity))
    }

    override fun onClick(view: View, position: Int) {
        if (position < 0 || position >= itemCount) {
            return
        }
    }

    private class ImpressionItemDelegate(private val activity: Activity) : BaseAdapterDelegate<ImpressionItem>(activity, R.layout.item_impression) {

        override fun onCreateViewHolder(itemView: View): RecyclerView.ViewHolder {
            return ViewHolder(itemView)
        }

        override fun isForViewType(item: ImpressionItem): Boolean {
            return true
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: ImpressionItem, payloads: List<*>) {
            holder as ViewHolder

            holder.tvTitle.text = item.title
            holder.rvImpressions.setHasFixedSize(true)
            holder.rvImpressions.adapter = ImpressionSubAdapter(activity, item.colors)
        }

        class ViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val tvTitle = itemView.tvTitle!!
            val rvImpressions = itemView.rvImpressions!!
        }
    }
}