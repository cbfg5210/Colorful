package com.ue.colorful.feature.calculate

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.view.View
import com.ue.adapterdelegate.BaseAdapterDelegate
import com.ue.adapterdelegate.DelegationAdapter
import com.ue.adapterdelegate.Item
import com.ue.adapterdelegate.OnDelegateClickListener
import com.ue.colorful.R
import com.ue.colorful.model.AlphaHex
import com.ue.colorful.model.AlphaHexTitle
import kotlinx.android.synthetic.main.item_alpha_hex.view.*

/**
 * Created by hawk on 2017/12/18.
 */
class AlphaHexAdapter(private val activity: Activity, items: List<Item>) : DelegationAdapter<Item>(), OnDelegateClickListener {

    init {
        if (items != null) this.items.addAll(items)

        this.addDelegate(AlphaHexDelegate(activity))
        this.addDelegate(AlphaHexTitleDelegate(activity))
    }

    override fun onClick(view: View, position: Int) {
        if (position < 0 || position >= itemCount) {
            return
        }
    }

    private class AlphaHexTitleDelegate(activity: Activity) : BaseAdapterDelegate<Item>(activity, R.layout.item_alpha_hex_title) {

        override fun onCreateViewHolder(itemView: View): RecyclerView.ViewHolder {
            return ViewHolder(itemView)
        }

        override fun isForViewType(item: Item): Boolean {
            return item is AlphaHexTitle
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Item, payloads: List<Any>) {
            holder as ViewHolder
            val alphaHex = item as AlphaHexTitle
            holder.tvAlpha.text = alphaHex.alphaTitle
            holder.tvHex.text = alphaHex.hexTitle
        }
    }

    private class AlphaHexDelegate(activity: Activity) : BaseAdapterDelegate<Item>(activity, R.layout.item_alpha_hex) {

        override fun onCreateViewHolder(itemView: View): RecyclerView.ViewHolder {
            return ViewHolder(itemView)
        }

        override fun isForViewType(item: Item): Boolean {
            return item is AlphaHex
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Item, payloads: List<Any>) {
            holder as ViewHolder
            item as AlphaHex
            holder.tvAlpha.text = item.alpha
            holder.tvHex.text = item.hex
        }
    }

    class ViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvAlpha = itemView.tvAlpha!!
        val tvHex = itemView.tvHex!!
    }
}