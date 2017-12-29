package com.ue.fingercoloring.feature.main

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import com.squareup.picasso.Picasso
import com.ue.fingercoloring.R
import com.ue.fingercoloring.feature.paint.PaintActivity
import com.ue.fingercoloring.model.LocalWork
import kotlinx.android.synthetic.main.view_localimage_item.view.*

internal class LocalPaintAdapter(var context: Context, localImageListBean: List<LocalWork>?) : RecyclerView.Adapter<LocalPaintAdapter.ViewHolder>() {
    val items = ArrayList<LocalWork>()
    var itemListener: AdapterView.OnItemClickListener? = null

    init {
        if (localImageListBean != null) items.addAll(localImageListBean)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(context).inflate(R.layout.view_localimage_item, parent, false)
        val holder = ViewHolder(v)

        holder.image.setOnClickListener {
            itemListener?.onItemClick(null, null, holder.adapterPosition, 0)

            val item = items[holder.adapterPosition]
            PaintActivity.start(context, false, item.imageName, item.imageUrl)
        }

        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val width = context.resources.displayMetrics.widthPixels / 5 * 3

        Picasso.with(context)
                .load(items[position].imageUrl)
                .resize(width, ((width / items[position].wvHRadio).toInt()))
                .tag(WorksFragment.TAG_WORKS)
                .into(holder.image)

        holder.lastModifyTime.text = context.getString(R.string.lastModifty, items[position].lastModDate)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    internal class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var image = itemView.ivThemeImage!!
        var lastModifyTime = itemView.lastModify!!
    }
}