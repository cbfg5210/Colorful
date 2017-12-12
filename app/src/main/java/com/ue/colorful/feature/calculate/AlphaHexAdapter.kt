package com.ue.colorful.feature.calculate

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ue.colorful.R
import com.ue.colorful.model.AlphaHex
import kotlinx.android.synthetic.main.item_alpha_hex.view.*

/**
 * Created by hawk on 2017/12/11.
 */
internal class AlphaHexAdapter(private val context: Context, private val alphaHexs: List<AlphaHex>) : RecyclerView.Adapter<AlphaHexAdapter.ViewHolder>() {

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        holder as ViewHolder
        val alphaHex = alphaHexs[position]
        holder.tvAlpha.text = "" + alphaHex.alpha
        holder.tvHex.text = alphaHex.hex
    }

    override fun getItemCount(): Int {
        return alphaHexs.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.item_alpha_hex, parent, false)
        return ViewHolder(itemView)
    }

    class ViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvAlpha = itemView.tvAlpha
        val tvHex = itemView.tvHex
    }
}