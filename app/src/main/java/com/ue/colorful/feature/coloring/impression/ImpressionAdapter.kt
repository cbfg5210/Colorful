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
import kotlinx.android.synthetic.main.item_impression_title.view.*
import org.greenrobot.eventbus.EventBus

/**
 * Created by hawk on 2017/12/22.
 */
internal class ImpressionAdapter(activity: Activity, items: List<Item>) : DelegationAdapter<Item>(), OnDelegateClickListener {

    init {
        this.items.addAll(items)

        val titleDelegate = TitleDelegate(activity)
        titleDelegate.onDelegateClickListener = this
        this.addDelegate(titleDelegate)

        val impressionDelegate = ImpressionDelegate(activity)
        impressionDelegate.onDelegateClickListener = this
        this.addDelegate(impressionDelegate)
    }

    fun getSpanSize(position: Int): Int {
        if (items[position] is ImpressionTitle) return 10
        if ((items[position] as ImpressionItem).hidden) return 1
        return 5
    }

    override fun onClick(view: View, position: Int) {
        if (position < 0 || position >= itemCount) {
            return
        }
        when (view.id) {
            R.id.vColor1,
            R.id.vColor2,
            R.id.vColor3 -> {
                val tag = view.tag as Int
                val item = items[position] as ImpressionItem
                EventBus.getDefault().post(SnackBarEvent(item.colors[tag]))
            }
            R.id.ivToggle -> {
                val titleItem = items[position] as ImpressionTitle
                (items[position] as ImpressionTitle).expandable = !titleItem.expandable

                val item = items[position + 1] as ImpressionItem
                val hidden = !item.hidden

                var i = 1
                while ((position + i < itemCount) && (items[position + i] !is ImpressionTitle))
                    (items[position + i++] as ImpressionItem).hidden = hidden

                notifyItemChanged(position, Object())
                notifyItemRangeChanged(position + 1, i - 1)
            }
        }
    }

    private class TitleDelegate(activity: Activity) : BaseAdapterDelegate<Item>(activity, R.layout.item_impression_title) {

        override fun onCreateViewHolder(itemView: View): RecyclerView.ViewHolder {
            val holder = ViewHolder(itemView)
            holder.ivToggle.setOnClickListener { v -> onDelegateClickListener?.onClick(v, holder.adapterPosition) }
            return holder
        }

        override fun isForViewType(item: Item): Boolean {
            return item is ImpressionTitle
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Item, payloads: List<Any>) {
            holder as ViewHolder
            item as ImpressionTitle

            holder.tvTitle.text = item.title
            holder.ivToggle.isSelected = item.expandable
        }

        class ViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val tvTitle = itemView.tvTitle!!
            val ivToggle = itemView.ivToggle!!
        }
    }

    private class ImpressionDelegate(activity: Activity) : BaseAdapterDelegate<Item>(activity, R.layout.item_impression) {

        override fun onCreateViewHolder(itemView: View): RecyclerView.ViewHolder {
            val holder = ViewHolder(itemView)

            val listener = View.OnClickListener { v -> onDelegateClickListener?.onClick(v, holder.adapterPosition) }
            for (i in holder.vColors.indices) {
                holder.vColors[i].tag = i
                holder.vColors[i].setOnClickListener(listener)
            }

            return holder
        }

        override fun isForViewType(item: Item): Boolean {
            return item is ImpressionItem
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Item, payloads: List<Any>) {
            holder as ViewHolder
            item as ImpressionItem

            for (i in holder.vColors.indices) {
                holder.vColors[i].setBackgroundColor(item.colors[i])
                holder.vColors[i].isEnabled = !item.hidden
            }
        }

        class ViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val vColors = arrayOf(itemView.vColor1!!, itemView.vColor2!!, itemView.vColor3!!)
        }
    }
}