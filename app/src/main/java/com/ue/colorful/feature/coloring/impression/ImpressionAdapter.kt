package com.ue.colorful.feature.coloring.impression

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ue.colorful.R
import com.ue.colorful.model.ImpressionItem
import kotlinx.android.synthetic.main.item_impression.view.*

/**
 * Created by hawk on 2017/12/21.
 */
class ImpressionAdapter(private val activity: Activity, mItems: List<ImpressionItem>) : RecyclerView.Adapter<ImpressionAdapter.ViewHolder>() {
    private val items = ArrayList<ImpressionItem>()

    init {
        if (mItems != null) items.addAll(mItems)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        holder as ViewHolder

        val item = items[position]
        holder.tvTitle.text = item.title
        holder.rvImpressions.setHasFixedSize(true)
        holder.rvImpressions.adapter = ImpressionSubAdapter(activity, item.colors)

        holder.ivToggle.setOnClickListener {
            holder.rvImpressions.visibility = if (holder.ivToggle.isSelected) View.VISIBLE else View.GONE
            holder.ivToggle.isSelected = !holder.ivToggle.isSelected
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_impression, null)
        return ViewHolder(itemView)
    }

    class ViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle = itemView.tvTitle!!
        val ivToggle = itemView.ivToggle!!
        val rvImpressions = itemView.rvImpressions!!
    }
}