package com.ue.colorful.feature.coloring.impression

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.view.View
import com.ue.adapterdelegate.BaseAdapterDelegate
import com.ue.adapterdelegate.DelegationAdapter
import com.ue.adapterdelegate.Item
import com.ue.adapterdelegate.OnDelegateClickListener
import com.ue.colorful.R
import com.ue.colorful.event.SnackBarEvent
import com.ue.colorful.model.ImpressionItem
import com.ue.colorful.model.ImpressionTitle
import kotlinx.android.synthetic.main.item_impression.view.*
import kotlinx.android.synthetic.main.item_sub_impression.view.*
import org.greenrobot.eventbus.EventBus

/**
 * Created by hawk on 2017/12/22.
 */
internal class ImpressionAdapter(activity: Activity, items: List<Item>) : DelegationAdapter<Item>(), OnDelegateClickListener {

    init {
        this.items = items

        val titleDelegate = TitleDelegate(activity)
        titleDelegate.setOnDelegateClickListener(this)
        this.addDelegate(titleDelegate)

        val impressionDelegate = ImpressionDelegate(activity)
        impressionDelegate.setOnDelegateClickListener(this)
        this.addDelegate(impressionDelegate)
    }

    override fun onClick(view: View, position: Int) {
        if (position < 0 || position >= itemCount) {
            return
        }
        when (view.id) {
            R.id.ivColor1,
            R.id.ivColor2,
            R.id.ivColor3,
            R.id.ivColor4,
            R.id.ivColor5,
            R.id.ivColor6 -> {
                val tag = view.tag as Int
                val item = items[position] as ImpressionItem
                if (tag < 3) EventBus.getDefault().post(SnackBarEvent(item.colors1[tag]))
                else EventBus.getDefault().post(SnackBarEvent(item.colors2!![tag - 3]))
            }
        }
    }

    private class TitleDelegate(activity: Activity) : BaseAdapterDelegate<Item>(activity, R.layout.item_impression_title) {

        override fun onCreateViewHolder(itemView: View): RecyclerView.ViewHolder {
            val holder = ViewHolder(itemView)
            holder.ivToggle.setOnClickListener { v ->
                holder.ivToggle.isSelected = !holder.ivToggle.isSelected
            }
            return holder
        }

        override fun isForViewType(item: Item): Boolean {
            return item is ImpressionTitle
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Item, payloads: List<*>) {
            holder as ViewHolder
            item as ImpressionTitle

            holder.tvTitle.text = item.title
        }

        class ViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val tvTitle = itemView.tvTitle!!
            val ivToggle = itemView.ivToggle!!
        }
    }

    private class ImpressionDelegate(activity: Activity) : BaseAdapterDelegate<Item>(activity, R.layout.item_sub_impression) {

        override fun onCreateViewHolder(itemView: View): RecyclerView.ViewHolder {
            val holder = ViewHolder(itemView)

            for (i in holder.ivColors.indices) {
                holder.ivColors[i].tag = i
                holder.ivColors[i].setOnClickListener({ v ->
                    onDelegateClickListener?.onClick(v, holder.adapterPosition)
                })
            }

            return holder
        }

        override fun isForViewType(item: Item): Boolean {
            return item is ImpressionItem
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Item, payloads: List<*>) {
            holder as ViewHolder
            item as ImpressionItem

            holder.ivColors[0].setBackgroundColor(item.colors1[0])
            holder.ivColors[1].setBackgroundColor(item.colors1[1])
            holder.ivColors[2].setBackgroundColor(item.colors1[2])
            if (item.colors2 == null) {
                holder.ivColors[3].visibility = View.INVISIBLE
                holder.ivColors[4].visibility = View.INVISIBLE
                holder.ivColors[5].visibility = View.INVISIBLE
            } else {
                holder.ivColors[3].visibility = View.VISIBLE
                holder.ivColors[4].visibility = View.VISIBLE
                holder.ivColors[5].visibility = View.VISIBLE
                holder.ivColors[3].setBackgroundColor(item.colors2[0])
                holder.ivColors[4].setBackgroundColor(item.colors2[1])
                holder.ivColors[5].setBackgroundColor(item.colors2[2])
            }
        }

        class ViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val ivColors = arrayOf(
                    itemView.ivColor1!!, itemView.ivColor2!!, itemView.ivColor3!!,
                    itemView.ivColor4!!, itemView.ivColor5!!, itemView.ivColor6!!)
        }
    }
}