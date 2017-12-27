package com.ue.fingercoloring.controller.main

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.ue.fingercoloring.R
import com.ue.fingercoloring.controller.paint.PaintActivity
import com.ue.fingercoloring.model.LocalWork
import com.ue.fingercoloring.util.PicassoUtils
import kotlinx.android.synthetic.main.view_localimage_item.view.*

/**
 * Created by Swifty.Wang on 2015/9/1.
 */
internal class LocalPaintAdapter(var context: Context, localImageListBean: List<LocalWork>?) : RecyclerView.Adapter<LocalPaintAdapter.ViewHolder>() {
    val items = ArrayList<LocalWork>()

    init {
        if (localImageListBean != null) items.addAll(localImageListBean)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(context).inflate(R.layout.view_localimage_item, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        PicassoUtils.displayImage(context, holder.image, items[position].imageUrl)

        val width = context.resources.displayMetrics.widthPixels / 5 * 3
        holder.image.layoutParams = LinearLayout.LayoutParams(width, (width / items[position].wvHRadio).toInt())
        holder.image.setOnClickListener {
            val item = items[holder.adapterPosition]
            PaintActivity.start(context, false, item.imageName, item.imageUrl)
        }
        holder.lastModifyTime.text = context.getString(R.string.lastModifty) + " " + items[position].lastModDate
    }

    override fun getItemCount(): Int {
        return items.size
    }

    internal class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var image = itemView.ivThemeImage!!
        var lastModifyTime = itemView.lastModify!!
    }
}