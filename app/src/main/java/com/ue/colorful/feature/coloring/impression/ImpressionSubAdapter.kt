/*
package com.ue.colorful.feature.coloring.impression

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ue.colorful.R
import com.ue.colorful.event.SnackBarEvent
import kotlinx.android.synthetic.main.item_sub_impression.view.*
import org.greenrobot.eventbus.EventBus

*/
/**
 * Created by hawk on 2017/12/21.
 *//*

class ImpressionSubAdapter(activity: Activity, items: List<IntArray>) : RecyclerView.Adapter<ImpressionSubAdapter.ViewHolder>() {
    private val items = ArrayList<IntArray>()

    init {
        if (items != null) this.items.addAll(items)
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        holder as ViewHolder
        val item = items[position]

        for (i in holder.ivColors.indices) {
            holder.ivColors[i].setBackgroundColor(item[i])
            holder.ivSelections[i].visibility = View.INVISIBLE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_impression, parent, false)
        val holder = ViewHolder(itemView)

        val listener = View.OnClickListener { v ->
            val tag = v.tag as Int
            EventBus.getDefault().post(SnackBarEvent(items[holder.adapterPosition][tag]))
        }

        for (i in holder.ivColors.indices) {
            holder.ivColors[i].tag = i
            holder.ivColors[i].setOnClickListener(listener)
        }

        return holder
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class ViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivColors = arrayOf(itemView.ivColor1!!, itemView.ivColor2!!, itemView.ivColor3!!)
        val ivSelections = arrayOf(itemView.ivSelected1!!, itemView.ivSelected2!!, itemView.ivSelected3!!)
    }
}*/
